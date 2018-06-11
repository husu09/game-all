package com.su.core.gambling;

import java.util.Arrays;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.su.common.util.CommonUtil;
import com.su.common.util.SpringUtil;
import com.su.common.util.TimeUtil;
import com.su.core.akka.AkkaContext;
import com.su.core.akka.TableActor;
import com.su.core.akka.TableActorImpl;
import com.su.core.context.PlayerContext;
import com.su.core.gambling.card.CardProcessor;
import com.su.core.gambling.card.CardProcessorManager;
import com.su.core.gambling.enums.CallType;
import com.su.core.gambling.enums.CardType;
import com.su.core.gambling.enums.MultipleType;
import com.su.core.gambling.enums.PlayerState;
import com.su.core.gambling.enums.TableState;
import com.su.core.gambling.enums.Team;
import com.su.core.gambling.service.NoticeService;
import com.su.msg.GamblingMsg._Card;
import com.su.msg.GamblingMsg._GamePlayer;
import com.su.msg.GamblingMsg._Table;

/**
 * 牌桌对象
 */
public class Table implements Delayed {
	private Logger logger = LoggerFactory.getLogger(Table.class);
	/**
	 * 牌
	 */
	private Card[] cards;
	/**
	 * 玩家
	 */
	private GamePlayer[] players;
	/**
	 * 状态
	 */
	private TableState state;
	/**
	 * 轮分
	 */
	private int roundScore;
	/**
	 * 被叫的牌
	 */
	private Card callCard;
	private CallType callType;
	private Integer callOp;
	/**
	 * 公共倍数
	 */
	private int[] multiples;
	/**
	 * 最后出牌
	 */
	private Card[] lastCards;
	private CardType lastCardType;
	private Integer lastOp;
	/**
	 * 庄家
	 */
	private int dealer;
	/**
	 * 结算后的等待时间
	 */
	private Long waitTime;
	/**
	 * 排名
	 */
	private Integer[] ranks;

	private Site site;

	private TableActor actor;

	private CardProcessorManager cardManager = SpringUtil.getContext().getBean(CardProcessorManager.class);
	private NoticeService noticeService = SpringUtil.getContext().getBean(NoticeService.class);
	
	/**
	 * 结算时等待时间
	 */
	private static final int CLOSE_WAIT_TIME = TimeUtil.ONE_SECOND * 15;
	/**
	 * 加倍时间
	 */
	private static final int DOUBLES_WAIT_TIME = TimeUtil.ONE_SECOND * 15;
	/**
	 * 参与玩家人数
	 */
	private static final int PLAYER_COUNT = 4;
	
	/**
	 * 通知
	 * */
	private _Table.Builder tableBuilder = _Table.newBuilder();
	private _GamePlayer.Builder gamePlayerBuilder = _GamePlayer.newBuilder();
	private _Card.Builder cardBuilder = _Card.newBuilder();

	public Table(Site site) {
		this.actor = AkkaContext.createActor(TableActor.class, TableActorImpl.class, this);
		this.site = site;
		// 初始化牌
		Card[] cards = new Card[Card.CARDS_NUM * 2];
		System.arraycopy(Card.ONE_CARDS, 0, cards, 0, Card.CARDS_NUM);
		System.arraycopy(Card.ONE_CARDS, 0, cards, Card.CARDS_NUM, Card.CARDS_NUM);
		this.cards = cards;
		this.multiples = new int[MultipleType.values().length];
		this.ranks = new Integer[PLAYER_COUNT];
	}

	/**
	 * 清空牌桌数据（解散牌桌时）
	 */
	public void clean() {
		reset();
		// 清空玩家
		for (int i = 0; i < this.players.length; i++)
			this.players[i] = null;
		// 庄家
		this.dealer = 0;
	}

	/**
	 * 重置牌桌状态（开始下一局时）
	 */
	public void reset() {
		// 轮分
		this.roundScore = 0;
		// 叫牌状态
		this.callCard = null;
		this.callType = null;
		this.callOp = null;
		// 重置倍数
		for (int i = 0; i < this.multiples.length; i++)
			this.multiples[i] = 0;
		// 最后出牌
		this.lastCards = null;
		this.lastCardType = null;
		this.lastOp = null;
		// 等待时间
		this.waitTime = null;
		// 重置玩家状态
		for (GamePlayer gamePlayer : this.players) {
			gamePlayer.reset();
		}
		// 排名
		for (int i = 0; i < this.ranks.length; i++)
			this.ranks[i] = null;
		// 状态
		this.state = null;
	}

	/**
	 * 洗牌
	 */
	private void shuffle() {
		for (int i = 0; i < this.cards.length; i++) {
			Card tmp = this.cards[i];
			int r = CommonUtil.range(i, this.cards.length);
			this.cards[i] = this.cards[r];
			this.cards[r] = tmp;
		}
	}

	/**
	 * 发牌
	 */
	public void deal() {
		// 洗牌
		shuffle();
		// 发牌
		int index = 0;
		for (int i = 1; i <= this.cards.length; i++) {
			this.players[(this.dealer + i - 1) % 4].getHandCards()[index] = this.cards[i];
			this.cards[i] = null;
			if (i % 4 == 0)
				index++;
		}
		for (int i = 0; i < this.players.length; i++) {
			Arrays.sort(this.players[i].getHandCards());
		}
		// 转换到时叫牌状态
		dealToCall();
		// 通知
		noticeService.noticeAllData(this);
	}

	/**
	 * 设置状态
	 */
	private void setState(TableState state) {
		if (this.state == TableState.DOUBLES || this.state == TableState.CLOSE)
			this.site.getWaitTableQueue().remove(this);
		this.state = state;
		if (this.state == TableState.DOUBLES)
			this.waitTime = TimeUtil.getCurrTime() + DOUBLES_WAIT_TIME;
		else if (this.state == TableState.CLOSE)
			this.waitTime = TimeUtil.getCurrTime() + CLOSE_WAIT_TIME;
		else
			return;
		this.site.getWaitTableQueue().put(this);
	}

	/**
	 * 加倍
	 */
	public boolean doubles(GamePlayer player, int multiple) {
		if (player.getState() != PlayerState.OPERATE) {
			logger.error("{} Player state is not operate {}", player.getId(), player.getState());
			return false;
		}
		if (state != TableState.DOUBLES) {
			logger.error("Table state is not doubles {} {}", state, Arrays.toString(players));
			return false;
		}
		player.setMultiple(player.getMultiple() + multiple);
		player.setState(PlayerState.WAIT);
		// 加倍结束切换状态
		if (isSameState(PlayerState.WAIT)) {
			dealToCall();
		}
		// 通知
		noticeService.noticeAllData(this);
		return true;
	}

	/**
	 * 加倍状态切换到叫牌状态
	 */
	private void dealToCall() {
		// 牌桌
		setState(TableState.CALL);
		// 玩家
		GamePlayer player = this.players[this.dealer];
		player.setState(PlayerState.OPERATE);
	}

	/**
	 * 是否是同一状态
	 */
	private boolean isSameState(PlayerState playerState) {
		for (GamePlayer otherPlayer : this.players) {
			if (otherPlayer.getState() != playerState)
				return false;
		}
		return true;
	}

	/**
	 * 过牌
	 */
	public void check(GamePlayer player) {
		if (player.getState() != PlayerState.OPERATE) {
			logger.error("{} Player state is not operate {}", player.getId(), player.getState());
			return;
		}
		player.setState(PlayerState.WAIT);

		if (state == TableState.CALL) {
			// 不叫
			// 叫牌结束切换状态
			if (isSameState(PlayerState.WAIT)) {
				callToDraw();
			}

		} else if (state == TableState.DOUBLES) {
			// 不加倍
			// 加倍结束切换状态
			if (isSameState(PlayerState.WAIT)) {
				dealToCall();
			}
		} else if (state == TableState.DRAW) {
			// 过牌
			// 处理下家
			GamePlayer nextPlayer = getNextPlayer(player);
			nextPlayer.setState(PlayerState.OPERATE);
		}
		// 通知
		noticeService.noticeAllData(this);
	}

	/**
	 * 叫牌
	 */
	public void call(GamePlayer player, int callTypeValue, int index) {
		if (player.getState() != PlayerState.OPERATE) {
			logger.error("{} Player state is not operate {}", player.getId(), player.getState());
			return;
		}
		if (this.state != TableState.CALL) {
			logger.error("Table state is not doubles {} {}", this.state, Arrays.toString(players));
			return;
		}
		CallType callType = CallType.get(callTypeValue);
		if (callType == null) {
			logger.error("{} CallType is null", player.getId());
			return;
		}
		Card card = player.getHandCards()[index];
		if (card == null) {
			logger.error("{} Card is null", player.getId());
			return;
		}
		if (!CallType.verify(callType, player.getHandCards(), card)) {
			logger.error("{} Call verify fail {}", player.getId(), card);
			return;
		}
		// 倍数
		addToMultiple(callType, cards);
		if (callType == CallType.LIGHT) {
			// 牌桌
			this.callCard = card;
			this.callType = callType;
			this.callOp = player.getIndex();
			setState(TableState.DRAW);
			// 当前玩家
			player.setTeam(Team.RED);
			player.setState(PlayerState.OPERATE);
			// 其它玩家
			for (GamePlayer gamePlayer : players) {
				if (gamePlayer.equals(player))
					continue;
				gamePlayer.setTeam(Team.BLUE);
				gamePlayer.setState(PlayerState.WAIT);
			}
		} else {
			// 只有庄家能叫牌，其它玩家只能明叫
			if (player.getIndex() != this.dealer) {
				logger.error("{} Player is not call {} {}", player.getId(), player.getIndex(), this.dealer);
				return;
			}
			// 牌桌
			this.callCard = card;
			if (CallType.isDark(card, player.getHandCards()))
				this.callType = CallType.DARK;
			else
				this.callType = callType;
			this.callOp = player.getIndex();
			// 当前玩家
			player.setTeam(Team.RED);
			player.setState(PlayerState.WAIT);
			// 全部操作完
			if (isSameState(PlayerState.WAIT)) {
				callToDraw();
			} else {
				// 下家
				GamePlayer nextPlayer = getNextPlayer(player);
				nextPlayer.setState(PlayerState.OPERATE);
			}
		}
		// 通知
		noticeService.noticeAllData(this);
	}

	/**
	 * 叫牌切换到出牌状态
	 */
	private void callToDraw() {
		// 没人叫牌则重新开始
		if (this.callOp == null) {
			reset();
			deal();
		} else {
			// 牌桌
			setState(TableState.DRAW);
			// 玩家
			GamePlayer player = this.players[this.callOp];
			player.setState(PlayerState.OPERATE);
		}
	}

	/**
	 * 增加公共倍数
	 */
	private void addToMultiple(Object o, Card[] cards) {
		MultipleType multiple = MultipleType.getMultiple(o, cards);
		if (multiple != null) {
			this.multiples[multiple.ordinal()] += multiple.getValue();
		}
	}

	/**
	 * 扣除公共倍数
	 */
	private void eddToMultiple(Object o, Card[] cards) {
		MultipleType multiple = MultipleType.getMultiple(o, cards);
		if (multiple != null && this.multiples[multiple.ordinal()] >= multiple.getValue()) {
			this.multiples[multiple.ordinal()] -= multiple.getValue();
		}
	}

	/**
	 * 出牌
	 */
	public void draw(GamePlayer player, int cardTypeValue, int[] indexs) {
		// 验证
		if (player.getState() != PlayerState.OPERATE) {
			logger.error("{} Player is not operate {}", player.getId(), player.getState());
			return;
		}
		if (state != TableState.DRAW) {
			logger.error("Table state is not doubles {} {}", state, Arrays.toString(players));
			return;
		}
		CardType cardType = CardType.get(cardTypeValue);
		if (cardType == null) {
			logger.error("{} CardType is null {}", player.getId(), cardTypeValue);
			return;
		}
		// 出牌
		Card[] cards = new Card[indexs.length];
		for (int i = 0; i < indexs.length; i++) {
			Card card = player.getHandCards()[indexs[i]];
			if (card == null) {
				logger.error("{} Indexs not match handCards {] {}", player.getId(), Arrays.toString(indexs),
						Arrays.toString(player.getHandCards()));
				return;
			}
			cards[i] = card;
			player.getHandCards()[indexs[i]] = null;
		}

		// 出牌验证
		CardProcessor cardProcessor = cardManager.getCardProcessor().get(cardType);
		if (!cardProcessor.verify(cards)) {
			logger.error("{} CardType not match Cards {} {}", player.getId(), cardTypeValue, Arrays.toString(indexs));
			return;
		}
		if (!cardProcessor.compare(cards, this.lastCardType, this.lastCards)) {
			logger.error("{} Cards not BG. lastCards {} {}", player.getId(), Arrays.toString(cards),
					Arrays.toString(this.lastCards));
			return;
		}
		// 当前玩家
		player.setState(PlayerState.WAIT);
		// 倍数
		addToMultiple(cardType, cards);
		// 轮分
		if (this.callType != CallType.LIGHT) {
			if (this.lastOp == player.getIndex() && this.roundScore != 0) {
				player.setScore(player.getScore() + this.roundScore);
				this.roundScore = 0;
			}
		}
		// 得分
		this.roundScore += Card.getScore(cards);
		// 出牌时处理玩家队伍
		if (player.getTeam() == null && this.callType != CallType.LIGHT) {
			for (Card card : cards) {
				if (card.equals(this.callCard)) {
					if (player.getIndex() != this.callOp) {
						// 叫牌
						GamePlayer callPlayer = this.players[this.callOp];
						player.setTeam(callPlayer.getTeam());
						for (GamePlayer otherPlayer : this.players) {
							if (otherPlayer.equals(player) || otherPlayer.equals(callPlayer))
								continue;
							otherPlayer.setTeam(Team.BLUE);
						}
					} else if (card != this.callCard && card.equals(this.callCard)) {
						// 暗叫
						eddToMultiple(CallType.CALL, null);
						addToMultiple(CallType.DARK, null);
						player.setScore(0);
						for (GamePlayer otherPlayer : this.players) {
							if (otherPlayer.equals(player))
								continue;
							otherPlayer.setTeam(Team.BLUE);
							// 清空积分
							otherPlayer.setScore(0);
						}
					}

				}
			}
		}
		// 牌桌
		this.lastCards = cards;
		this.lastCardType = cardType;
		this.lastOp = player.getIndex();
		if (!doTeamRankClose(player)) {
			// 游戏未结束，由下家出牌
			GamePlayer nextPlayer = getNextPlayer(player);
			nextPlayer.setState(PlayerState.OPERATE);
			// 托管状态自行出牌
			if (nextPlayer.isAuto() == 1) {
				check(nextPlayer);
			}
		}

		// 记录出牌
		for (int i = 0; i < cards.length; i ++) {
			for (Card card : this.cards) {
				if (card == null)
					card = cards[i];
			}
		}
		
		// 通知
		noticeService.noticeAllData(this);
	}

	/**
	 * 获取下家
	 */
	private GamePlayer getNextPlayer(GamePlayer player) {
		GamePlayer nextPlayer = null;
		if (player.getState() == PlayerState.FINISH) {
			for (GamePlayer otherPlayer : this.players) {
				if (player.equals(otherPlayer))
					continue;
				if (player.getTeam() == otherPlayer.getTeam()) {
					nextPlayer = otherPlayer;
					break;
				}
			}
		} else {
			nextPlayer = players[player.getIndex() + 1 % 4];
		}
		return nextPlayer;

	}

	/**
	 * 添加排行
	 */
	private int addToRanks(GamePlayer player) {
		int rank = -1;
		for (int i = 0; i < this.ranks.length; i++) {
			if (this.ranks[i] == null) {
				this.ranks[i] = player.getIndex();
				rank = i;
				break;
			}
		}
		if (rank == -1) {
			logger.error("{} Rank is error {}", player.getId(), rank);
		}
		return rank;
	}

	/**
	 * 排行数量
	 */
	private int getRanksCount() {
		int count = 0;
		for (int i = 0; i < this.ranks.length; i++) {
			if (this.ranks[i] != null)
				count++;
			else
				break;
		}
		return count;
	}

	/**
	 * 是否完成
	 */
	private boolean isFinish(GamePlayer player) {
		for (Card card : player.getHandCards()) {
			if (card != null)
				return false;
		}
		return true;
	}

	/**
	 * 处理队伍、排名、结束
	 */
	private boolean doTeamRankClose(GamePlayer player) {
		if (isFinish(player)) {
			player.setState(PlayerState.FINISH);
			// 全部出完后还没有队伍，则可以判断是蓝方
			if (player.getTeam() == null)
				player.setTeam(Team.BLUE);
			// 排名
			addToRanks(player);
			// 判断牌局是否结束
			// 处理玩家队伍和排名
			Team winTeam = null;
			if (this.callType == CallType.LIGHT || this.callType == CallType.DARK) {
				winTeam = this.players[ranks[0]].getTeam();
				for (GamePlayer otherPlayer : this.players) {
					if (player.getTeam() == null)
						player.setTeam(Team.BLUE);
					otherPlayer.setState(PlayerState.FINISH);
					// 排名
					addToRanks(otherPlayer);
				}
			} else if (this.ranks[0] != null && this.ranks[1] != null
					&& this.players[ranks[0]].getTeam() == this.players[ranks[1]].getTeam()) {
				winTeam = this.players[ranks[0]].getTeam();
				for (GamePlayer otherPlayer : this.players) {
					if (player.getTeam() == null)
						player.setTeam(this.players[ranks[0]].getTeam().getReverse());
					otherPlayer.setState(PlayerState.FINISH);
					// 排名
					addToRanks(otherPlayer);
				}
				// 全胜
				addToMultiple(MultipleType.QUAN_SHENG, null);
			} else if (getRanksCount() >= 3) {
				GamePlayer lastPlayer = getLastPlayer();
				if (player.getTeam() == null)
					player.setTeam(this.players[this.ranks[2]].getTeam().getReverse());
				lastPlayer.setState(PlayerState.FINISH);
				// 排名
				addToRanks(lastPlayer);
				/*
				 * // 最后出牌玩家分数处理
				 * this.players[this.ranks[0]].addScore(lastPlayer.getScore());
				 * lastPlayer.setScore(0); int leftScore =
				 * Card.getScore(lastPlayer.getHandCards());
				 * this.players[this.ranks[1]].addScore(leftScore);
				 */
				// 最后玩家得分作废
				lastPlayer.setScore(0);

				// 最后剩余轮分处理
				if (this.roundScore != 0) {
					player.setScore(player.getScore() + this.roundScore);
					this.roundScore = 0;
				}
				// 计算总分
				int redScore = 0;
				int blueScore = 0;
				for (GamePlayer otherPlayer : this.players) {
					if (otherPlayer.getTeam() == Team.RED)
						redScore += otherPlayer.getScore();
					else
						blueScore += otherPlayer.getScore();
				}
				if (redScore > blueScore) {
					winTeam = Team.RED;
				} else if (redScore == blueScore) {
					winTeam = this.players[ranks[0]].getTeam();
				} else {
					winTeam = Team.BLUE;
				}
				// 满分
				/*
				 * if (redScore >= 200 || blueScore >= 200)
				 * addToComMultiple(MultipleType.MAN_FEN, null);
				 */
			}
			if (winTeam != null) {
				close(winTeam);
				return true;
			}
		}
		return false;
	}

	/**
	 * 结算
	 */
	private void close(Team winTeam) {
		// 计算倍数
		int sumMultiple = 0;
		for (int multiple : this.multiples) {
			sumMultiple += multiple;
		}
		for (GamePlayer otherPlayer : this.players) {
			sumMultiple += otherPlayer.getMultiple();
		}
		int redMultiple = sumMultiple / getTeamCount(Team.RED);
		int blueMultiple = sumMultiple / getTeamCount(Team.BLUE);
		// 玩家结算
		for (GamePlayer otherPlayer : this.players) {
			TableResult tableResult = new TableResult();
			tableResult.setPlayerContext(otherPlayer.getPlayerContext());
			tableResult.setSiteCo(this.site.getSiteCo());
			if (otherPlayer.getTeam() == winTeam)
				tableResult.setWin(true);

			else
				tableResult.setWin(false);

			if (otherPlayer.getTeam() == Team.RED)
				tableResult.setMultiple(redMultiple);
			else
				tableResult.setMultiple(blueMultiple);
			otherPlayer.getPlayerContext().getActor().doTableResult(tableResult);
		}
		// 下局的庄家
		for (int i = 0 ; i < this.ranks.length; i ++ ) {
			if (this.players[this.ranks[i]] .getTeam() == winTeam) {
				this.dealer = this.players[this.ranks[i]].getIndex();
				break;
			}
		}
		// 牌桌
		setState(TableState.CLOSE);
	}

	/**
	 * 获取任意一方的人数
	 */
	private int getTeamCount(Team team) {
		int count = 0;
		for (GamePlayer otherPlayer : this.players) {
			if (team == otherPlayer.getTeam())
				count++;
		}
		return count;
	}

	/**
	 * 获取最后名的玩家
	 */
	private GamePlayer getLastPlayer() {
		GamePlayer lastPlayer = null;
		for (GamePlayer otherPlayer : this.players) {
			if (otherPlayer.getState() != PlayerState.FINISH) {
				lastPlayer = otherPlayer;
				break;
			}
		}
		return lastPlayer;
	}
	
	/**
	 * 设置托管
	 * */
	public void setIsAuto(GamePlayer player, int isAuto) {
		if (player.getState() != PlayerState.OPERATE) {
			logger.error("{} Player is not operate {}", player.getId(), player.getState());
			return;
		}
	
	}

	/**
	 * 退出
	 */
	public void exit(GamePlayer gamePlayer) {
		// 自已退出清空数据
		gamePlayer.clean();
		// 其他玩家重新设置状态加入匹配
		for (GamePlayer otherPlayer : this.players) {
			if (gamePlayer.equals(otherPlayer))
				continue;
			otherPlayer.clean();
			this.site.getPlayerDeque().offerFirst(otherPlayer);
		}
		// 牌桌
		clean();
		this.site.getWaitTableQueue().remove(this);
		this.site.getIdleTableQueue().offer(this);
		// 通知
		noticeService.noticeAllData(this);
	}

	/**
	 * 准备
	 */
	public void ready(GamePlayer player) {
		if (player.getState() != PlayerState.OPERATE) {
			logger.error("{} Player state is not operate {}", player.getId(), player.getState());
			return;
		}
		if (state != TableState.CLOSE) {
			logger.error("Table state is not doubles {} {}", state, Arrays.toString(players));
			return;
		}
		player.setState(PlayerState.READY);
		// 全部准备，开始游戏
		if (isSameState(PlayerState.READY)) {
			reset();
			deal();
		}
		// 通知
		noticeService.noticeAllData(this);
	}

	/**
	 * 重连
	 */
	public void reconnect(GamePlayer gamePlayer) {
		gamePlayer.setAuto(0);
		// 通知
		noticeService.noticeAllData(this);
	}

	/**
	 * 玩家超时
	 */
	public void doWaitGamePlayer(GamePlayer gamePlayer) {
		if (gamePlayer.getState() != PlayerState.OPERATE)
			return;
		gamePlayer.setState(PlayerState.WAIT);
		// 处理下家
		GamePlayer nextPlayer = getNextPlayer(gamePlayer);
		nextPlayer.setState(PlayerState.OPERATE);
		// 通知
		noticeService.noticeAllData(this);
	}

	/**
	 * 等待超时
	 */
	public void doWaitTable() {
		if (this.getState() == TableState.DOUBLES) {
			// 加倍超时
			for (GamePlayer otherGamePlayer : this.players) 
				otherGamePlayer.setState(PlayerState.WAIT);
			dealToCall();
		} else if (this.getState() == TableState.CLOSE) {
			// 结算超时
			for (GamePlayer otherGamePlayer : this.players) 
				otherGamePlayer.clean();
			// 牌桌
			clean();
			this.site.getIdleTableQueue().offer(this);
		}
		// 通知
		noticeService.noticeAllData(this);
	}


	public void setPlayers(GamePlayer[] players) {
		if (this.players != null)
			throw new RuntimeException("error operate");
		// 玩家数据
		for (int i = 0; i < players.length; i++) {
			players[i].setIndex(i);
			players[i].setTable(this);
		}
		this.players = players;
	}

	@Override
	public int compareTo(Delayed o) {
		if (this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)) {
			return 1;
		} else if (this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS)) {
			return -1;
		}
		return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(waitTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

	public GamePlayer[] getPlayers() {
		return players;
	}

	public TableState getState() {
		return state;
	}

	public int getRoundScore() {
		return roundScore;
	}

	public Card getCallCard() {
		return callCard;
	}

	public CallType getCallType() {
		return callType;
	}

	public Integer getCallOp() {
		return callOp;
	}

	public int[] getMultiples() {
		return multiples;
	}

	public Card[] getLastCards() {
		return lastCards;
	}

	public CardType getLastCardType() {
		return lastCardType;
	}

	public Integer getLastOp() {
		return lastOp;
	}

	public int getDealer() {
		return dealer;
	}

	public Long getWaitTime() {
		return waitTime;
	}

	public Integer[] getRanks() {
		return ranks;
	}

	public Site getSite() {
		return site;
	}

	public TableActor getActor() {
		return actor;
	}

	public _Table.Builder getTableBuilder() {
		return tableBuilder;
	}

	public _GamePlayer.Builder getGamePlayerBuilder() {
		return gamePlayerBuilder;
	}

	public _Card.Builder getCardBuilder() {
		return cardBuilder;
	}
	
	
}
