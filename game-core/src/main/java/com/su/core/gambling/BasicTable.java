package com.su.core.gambling;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.su.common.util.CommonUtil;
import com.su.common.util.TimeUtil;
import com.su.core.akka.AkkaContext;
import com.su.core.akka.TableActor;
import com.su.core.akka.TableActorImpl;
import com.su.core.gambling.assist.MultipleAssist;
import com.su.core.gambling.assist.RankAssist;
import com.su.core.gambling.assist.TableAssist;
import com.su.core.gambling.assist.TeamAssist;
import com.su.core.gambling.assist.card.CardAssist;
import com.su.core.gambling.assist.card.CardAssistManager;
import com.su.core.gambling.assist.notice.BasicNoticeAssist;
import com.su.core.gambling.card.CardProcessor;
import com.su.core.gambling.enums.CallType;
import com.su.core.gambling.enums.CardType;
import com.su.core.gambling.enums.MultipleType;
import com.su.core.gambling.enums.PlayerState;
import com.su.core.gambling.enums.TableState;
import com.su.core.gambling.enums.Team;
import com.su.msg.GamblingMsg.Match_;
import com.su.msg.GamblingMsg.Quit_;
import com.su.msg.GamblingMsg.TableResult_;

public abstract class BasicTable implements Delayed {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
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
	private Integer dealer;
	/**
	 * 结算后的等待时间
	 */
	private Long waitTime;
	/**
	 * 排名
	 */
	private Integer[] ranks;
	/**
	 * 房间
	 */
	private BasicRoom room;
	/**
	 * 通知
	 */
	private BasicNoticeAssist noticeAssist;
	/**
	 * actor
	 */
	private BasicTable actor;
	/**
	 * 辅助类
	 */
	private CardAssistManager cardAssistManager;
	private TableAssist tableAssist;
	private MultipleAssist multipleAssist;
	private TeamAssist teamAssist;
	private RankAssist rankAssist;

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

	public BasicTable(BasicRoom room) {
		this.actor = AkkaContext.createActor(BasicTable.class, this.getClass(), this);
		this.room = room;
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
		// 状态
		this.state = null;
		// 清空玩家
		for (int i = 0; i < this.players.length; i++)
			this.players[i] = null;
		// 庄家
		this.dealer = null;
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
	}
	
	/**
	 * 设置玩家
	 * */
	public void setPlayers(GamePlayer[] players) {
		// 玩家数据
		for (int i = 0; i < players.length; i++) {
			players[i].setIndex(i);
			players[i].setTable(this);
		}
		this.players = players;
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

	public void deal() {
		// 洗牌
		shuffle();
		// 发牌
		int index = 0;
		for (int i = 1; i <= this.cards.length; i++) {
			this.players[(this.dealer + i) % 4 - 1].getHandCards()[index] = this.cards[i];
			this.cards[i] = null;
			if (i % 4 == 0)
				index++;
		}
		for (int i = 0; i < this.players.length; i++) {
			Arrays.sort(this.players[i].getHandCards(), Collections.reverseOrder());
		}
		// 转换到叫牌状态
		setState(TableState.CALL);
		this.players[this.dealer].setState(PlayerState.OPERATE);
		// 通知
		notice.notice(this);
	}

	@Override
	public void call(GamePlayer player, int callTypeValue, int index) {
		if (player.getState() != PlayerState.OPERATE) {
			logger.error("Player state is not operate {} {}", player.getState(), player.getId());
			return;
		}
		if (this.state != TableState.CALL) {
			logger.error("Table state is not call {} {}", this.state, player.getId());
			return;
		}
		CallType callType = CallType.get(callTypeValue);
		if (callType == null) {
			logger.error("CallType is null {} {}", callTypeValue, player.getId());
			return;
		}
		Card card = player.getHandCards()[index];
		if (!CallType.verify(callType, player.getHandCards(), card)) {
			logger.error("Call verify fail {} {} {}", callType, card, player.getId());
			return;
		}
		// 只有庄家能叫牌，其它玩家只能明叫
		if (callType != CallType.LIGHT && player.getIndex() != this.dealer) {
			logger.error("Player is not call {} {} {} {}", callType, this.dealer, player.getIndex(), player.getId());
			return;
		}
		// 牌桌
		this.callCard = card;
		this.callType = callType;
		this.callOp = player.getIndex();
		// 倍数
		multipleService.addMultiple(this, callType, null);
		if (callType == CallType.LIGHT) {
			// 当前玩家
			player.setTeam(Team.RED);
			// 其它玩家
			for (GamePlayer gamePlayer : players) {
				if (gamePlayer.equals(player))
					continue;
				gamePlayer.setTeam(Team.BLUE);
			}
			callToDoubles();
		} else {
			// 当前玩家
			player.setTeam(Team.RED);
			player.setState(PlayerState.WAIT);
			// 全部操作完
			if (isSameState(PlayerState.WAIT))
				callToDoubles();
			else
				doNextPlayerState(player);
		}
		// 通知
		notice.notice(this);
	}

	@Override
	public boolean doubles(GamePlayer player, int multiple) {
		if (player.getState() != PlayerState.OPERATE) {
			logger.error("Player state is not operate {} {}", player.getState(), player.getId());
			return false;
		}
		if (this.state != TableState.DOUBLES) {
			logger.error("Table state is not allow {} {}", this.state, player.getId());
			return false;
		}
		player.addMultiple(multiple);
		player.setState(PlayerState.WAIT);
		// 加倍结束切换状态
		if (isSameState(PlayerState.WAIT)) {
			doublesToDraw();
		}
		// 通知
		notice.notice(this);
		return true;
	}

	@Override
	public void check(GamePlayer player) {
		if (player.getState() != PlayerState.OPERATE) {
			logger.error("Player state is not operate {} {}", player.getState(), player.getId());
			return;
		}
		player.setState(PlayerState.WAIT);

		if (this.state == TableState.CALL) {
			// 不叫
			// 叫牌结束切换状态
			if (isSameState(PlayerState.WAIT))
				callToDoubles();

		} else if (this.state == TableState.DOUBLES) {
			// 不加倍
			// 加倍结束切换状态
			if (isSameState(PlayerState.WAIT))
				doublesToDraw();

		} else if (this.state == TableState.DRAW) {
			// 过牌
			// 处理下家
			doNextPlayerState(player);
		} else {
			logger.error("Table state is not allow {} {}", this.state, player.getId());
			return;
		}
		// 通知
		notice.notice(this);
	}

	@Override
	public void draw(GamePlayer player, int cardTypeValue, int[] indexs) {
		// 验证
		if (player.getState() != PlayerState.OPERATE) {
			logger.error("Player state is not operate {} {}", player.getState(), player.getId());
			return;
		}
		if (state != TableState.DRAW) {
			logger.error("Table state is not allow {} {}", this.state, player.getId());
			return;
		}
		CardType cardType = CardType.get(cardTypeValue);
		if (cardType == null) {
			logger.error("CardType is null {} {}", cardTypeValue, player.getId());
			return;
		}
		// 出牌
		Card[] cards = new Card[indexs.length];
		for (int i = 0; i < indexs.length; i++) {
			Card card = player.getHandCards()[indexs[i]];
			if (card == null) {
				logger.error("Indexs not match handCards {} {}", indexs[i], player.getId());
				return;
			}
			cards[i] = card;
			player.getHandCards()[indexs[i]] = null;
		}

		// 出牌验证
		CardProcessor cardProcessor = cardService.getCardProcessor().get(cardType);
		if (!cardProcessor.verify(cards)) {
			logger.error(" CardType not match Cards {} {}", cardTypeValue, player.getId());
			return;
		}
		if (!cardProcessor.compare(cards, this.lastCardType, this.lastCards)) {
			logger.error("Cards not BG. lastCards {} {} {}", Arrays.toString(cards), Arrays.toString(this.lastCards),
					player.getId());
			return;
		}
		// 当前玩家
		player.setState(PlayerState.WAIT);
		// 倍数
		multipleService.addMultiple(this, cardType, cards);
		// 轮分
		if (this.callType != CallType.LIGHT) {
			if (this.lastOp == player.getIndex() && this.roundScore != 0) {
				player.setScore(player.getScore() + this.roundScore);
				this.roundScore = 0;
			}
		}
		// 得分
		this.roundScore += Card.getScore(cards);
		// 牌桌
		this.lastCards = cards;
		this.lastCardType = cardType;
		this.lastOp = player.getIndex();
		if (doTeamRankClose(player)) {
			// 游戏结束，所有玩家准备
			for (GamePlayer otherPlayer : this.players)
				otherPlayer.setState(PlayerState.OPERATE, false);
		} else {
			// 游戏未结束，由下家出牌
			doNextPlayerState(player);
		}

		// 记录出牌
		for (int i = 0; i < cards.length; i++) {
			for (Card card : this.cards) {
				if (card == null)
					card = cards[i];
			}
		}

		// 通知
		noticeService.noticeAllData(this);
	}

	@Override
	public void close() {

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
		TableResult_.Builder builder = TableResult_.newBuilder();
		builder.setBaseScore(this.site.getSiteCo().getBaseScore());
		builder.setWinTeam(winTeam.getValue());
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
			builder.addPlayerResult(otherPlayer.getPlayerContext().getActor().doTableResult(tableResult));
		}
		// 下局的庄家
		for (int i = 0; i < this.ranks.length; i++) {
			if (this.players[this.ranks[i]].getTeam() == winTeam) {
				this.dealer = this.players[this.ranks[i]].getIndex();
				break;
			}
		}
		// 牌桌
		setState(TableState.CLOSE);
		// 通知
		for (GamePlayer otherPlayer : this.players) {
			otherPlayer.getPlayerContext().write(builder);
		}

	}

	@Override
	public void ready(GamePlayer player) {
		if (player.getState() != PlayerState.OPERATE) {
			logger.error("Player state is not operate {} {}", player.getState(), player.getId());
			return;
		}
		if (this.state != TableState.CLOSE) {
			logger.error("Table state is not allow {} {}", this.state, player.getId());
			return;
		}
		player.setState(PlayerState.READY);
		// 全部准备，开始游戏
		if (isSameState(PlayerState.READY)) {
			reset();
			deal();
		}
		// 通知
		notice.notice(this);
	}

	@Override
	public void exit(GamePlayer gamePlayer) {
		// 自已退出清空数据
		gamePlayer.clean();
		gamePlayer.setState(null);
		if (state == TableState.CLOSE) {
			// 其他玩家重新设置状态加入匹配
			for (GamePlayer otherPlayer : this.players) {
				if (gamePlayer.equals(otherPlayer))
					continue;
				otherPlayer.clean();
				this.site.addPlayerToMatch(otherPlayer.getPlayerContext(), true);
			}
			// 牌桌
			clean();
			this.site.getWaitTableQueue().remove(this);
			this.site.getIdleTableQueue().offer(this);
		} else {
			gamePlayer.setAuto(1);
			if (gamePlayer.getState() == PlayerState.OPERATE)
				check(gamePlayer);
			doNextPlayerState(gamePlayer);
		}
		// 通知
		noticeService.noticeAllData(this);

	}

	@Override
	public void reconnect(GamePlayer gamePlayer) {
		gamePlayer.setAuto(0);
		// 通知
		notice.notice(this);
	}

	@Override
	public void doWaitGamePlayer(GamePlayer gamePlayer) {
		if (gamePlayer.getState() != PlayerState.OPERATE)
			return;
		gamePlayer.setState(PlayerState.WAIT);
		doNextPlayerState(gamePlayer);
		// 通知
		notice.notice(this);
	}

	@Override
	public void doWaitTable() {
		if (this.state == TableState.DOUBLES) {
			// 加倍超时
			for (GamePlayer otherGamePlayer : this.players)
				otherGamePlayer.setState(PlayerState.WAIT);
			doublesToDraw();
		} else if (this.state == TableState.CLOSE) {
			// 结算超时
			for (GamePlayer otherGamePlayer : this.players) {
				if (otherGamePlayer.getState() != PlayerState.READY) {
					otherGamePlayer.clean();
					// TODO
					otherGamePlayer.getPlayerContext().write(Quit_.newBuilder());
				} else {
					// TODO
					// 准备中的玩家重新加入匹配队列
					otherGamePlayer.clean();
					this.site.addPlayerToMatch(otherGamePlayer.getPlayerContext(), true);
					otherGamePlayer.getPlayerContext().write(Match_.newBuilder());
				}
			}
			// TODO
			// 牌桌
			clean();
			this.site.getIdleTableQueue().offer(this);
		}

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
				// 满分
				int winScore = 0;
				for (GamePlayer otherPlayer : this.players) {
					if (otherPlayer.getTeam() == winTeam)
						winScore += otherPlayer.getScore();
				}
				if (winScore >= 200)
					addToMultiple(MultipleType.MAN_FEN, null);
			} else if (getRanksCount() >= 3) {
				GamePlayer lastPlayer = getLastPlayer();
				if (player.getTeam() == null)
					player.setTeam(this.players[this.ranks[2]].getTeam().getReverse());
				lastPlayer.setState(PlayerState.FINISH);
				// 排名
				addToRanks(lastPlayer);

				// 最后出牌玩家分数处理
				// 手中的分数给第一名
				this.players[this.ranks[0]].addScore(lastPlayer.getScore());
				// 最后玩家得分作废
				lastPlayer.setScore(0);
				// 未出的分数直接加个对方
				int leftScore = Card.getScore(lastPlayer.getHandCards());

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
				if (lastPlayer.getTeam() == Team.RED)
					blueScore += leftScore;
				else
					redScore += blueScore;
				if (redScore > blueScore) {
					winTeam = Team.RED;
				} else if (redScore == blueScore) {
					winTeam = this.players[ranks[0]].getTeam();
				} else {
					winTeam = Team.BLUE;
				}
				// 满分
				if (redScore >= 200 || blueScore >= 200)
					addToMultiple(MultipleType.MAN_FEN, null);

			}
			if (winTeam != null) {
				close(winTeam);
				return true;
			}
		}
		return false;
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
	 * 加倍状态切换到出牌状态
	 */
	private void doublesToDraw() {
		// 牌桌
		setState(TableState.DRAW);
		// 玩家
		GamePlayer player = this.players[this.callOp];
		player.setState(PlayerState.OPERATE);
	}

	/**
	 * 叫牌切换到加倍状态
	 */
	private void callToDoubles() {
		// 没人叫牌则重新开始
		if (this.callOp == null) {
			reset();
			deal();
		} else {
			// 牌桌
			setState(TableState.DOUBLES);
			// 玩家
			for (GamePlayer otherPlayer : this.players)
				otherPlayer.setState(PlayerState.OPERATE, false);
		}
	}

	/**
	 * 处理下家状态
	 */
	private void doNextPlayerState(GamePlayer player) {
		GamePlayer nextPlayer = null;
		if (player.getState() == PlayerState.FINISH) {
			// 队友接风
			for (GamePlayer otherPlayer : this.players) {
				if (player.equals(otherPlayer))
					continue;
				if (player.getTeam() == otherPlayer.getTeam()) {
					nextPlayer = otherPlayer;
					break;
				}
			}
			// 没有队友时下家接风
			if (nextPlayer == null)
				nextPlayer = players[player.getIndex() + 1 % 4];
		} else {
			nextPlayer = players[player.getIndex() + 1 % 4];
		}
		nextPlayer.setState(PlayerState.OPERATE);
		// 托管状态自行出牌 XXX 后端直接做没有动画表现
		if (nextPlayer.isAuto() == 1) {
			check(nextPlayer);
		}
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
	 * 设置状态
	 */
	private void setState(TableState state) {
		if (this.state == TableState.DOUBLES || this.state == TableState.CLOSE)
			this.room.getWaitTableQueue().remove(this);
		this.state = state;
		if (this.state == TableState.DOUBLES)
			this.waitTime = TimeUtil.getCurrTime() + DOUBLES_WAIT_TIME;
		else if (this.state == TableState.CLOSE)
			this.waitTime = TimeUtil.getCurrTime() + CLOSE_WAIT_TIME;
		else
			return;
		this.room.getWaitTableQueue().put(this);
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

	public Integer getDealer() {
		return dealer;
	}

	public void setDealer(Integer dealer) {
		this.dealer = dealer;
	}

	public Card[] getCards() {
		return cards;
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

	public Integer[] getRanks() {
		return ranks;
	}

	public BasicRoom getRoom() {
		return room;
	}

	public BasicTable getActor() {
		return actor;
	}

}
