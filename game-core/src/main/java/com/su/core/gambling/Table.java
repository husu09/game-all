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
import com.su.core.gambling.util.MultipleUtil;

/**
 * 牌桌对象
 */
public class Table implements Delayed{
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
	 * */
	private Integer[] ranks;
	
	private Site site;
	
	private TableActor actor;

	private AkkaContext akkaContext = SpringUtil.getContext().getBean(AkkaContext.class);
	private CardProcessorManager cardManager = SpringUtil.getContext().getBean(CardProcessorManager.class);
	
	private Logger logger = LoggerFactory.getLogger(Table.class);
	
	/**
	 * 叫牌时间
	 * */
	private static final int CALL_WAIT_TIME  = TimeUtil.ONE_SECOND * 15;
	/**
	 * 出牌时间
	 * */
	private static final int OPERATE_WAIT_TIME  = TimeUtil.ONE_SECOND * 15;
	/**
	 * 结算时等待时间
	 * */
	private static final int CLOSE_WAIT_TIME = TimeUtil.ONE_SECOND * 15;
	/**
	 * 加倍时间
	 * */
	private static final int DOUBLES_WAIT_TIME = TimeUtil.ONE_SECOND * 15;
	
	public Table(Site site) {
		actor = akkaContext.createActor(TableActor.class, TableActorImpl.class, this);
		this.site = site;
		// 初始化牌
		Card[] cards = new Card[Card.CARDS_NUM * 2];
		System.arraycopy(Card.ONE_CARDS, 0, cards, 0, Card.CARDS_NUM);
		System.arraycopy(Card.ONE_CARDS, 0, cards, Card.CARDS_NUM, Card.CARDS_NUM);
		this.cards = cards;
		multiples = new int[MultipleType.values().length];
		this.ranks = new Integer[4];
	}
	
	/**
	 * 清空牌桌数据（解散牌桌时）
	 * */
	public void clean() {
		reset();
		// 清空玩家
		for (int i = 0; i < this.players.length; i ++) 
			this.players[i] = null;
	}
	
	/**
	 * 重置牌桌状态（开始下一局时）
	 * */
	public void reset() {
		roundScore = 0;
		// 叫牌状态
		callCard = null;
		callType = null;
		// 重置倍数
		for (int i = 0; i < multiples.length; i ++) {
			multiples[i] = 0;
		}
		// 最后出牌
		lastCards = null;
		lastCardType = null;
		lastOp = null;
		// 等待时间
		waitTime = null;
		// 重置玩家状态
		for (GamePlayer gamePlayer : players) {
			gamePlayer.reset();
		}
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
		for (int i = 1; i <= cards.length; i++) {
			players[(dealer + i - 1) % 4].getHandCards()[index] = cards[i];
			if (i % 4 == 0)
				index++;
		}
		for (int i = 0; i < players.length; i++) {
			Arrays.sort(players[i].getHandCards());
		}
		// 设置牌桌状态
		state = TableState.CALL;
		// 设置玩家
		for (GamePlayer gamePlayer : players) {
			gamePlayer.setState(PlayerState.OPERATE);
		}
		waitTime  = TimeUtil.getCurrTime() + DOUBLES_WAIT_TIME;
		site.getWaitTableQueue().put(this);
		// TODO 通知
	}
	
	/**
	 * 加倍
	 * */
	public boolean doubles(GamePlayer player, int multiple) {
		if (player.getState() != PlayerState.OPERATE) {
			logger.error("{} Player state is not operate {}", player.getId(), player.getState());
			return false;
		}
		if (state != TableState.DOUBLES) {
			logger.error("Table state is not doubles {} {}", state, Arrays.toString(players));
			return false;
		}
		player.setMultipleValue(player.getMultipleValue() + multiple);
		player.setState(PlayerState.WAIT);
		this.site.getWaitGamePlayerQueue().remove(player);
		// 加倍结束切换状态
		if (isSameState(PlayerState.WAIT)) {
			doublesToDraw();
		}
		//TODO 通知
		return true;
	}
	
	/**
	 * 加倍状态切换到出牌状态
	 * */
	private void doublesToDraw(){
		GamePlayer player = this.players[this.callOp];
		player.setState(PlayerState.OPERATE);
		player.setOpTime(TimeUtil.getCurrTime() + OPERATE_WAIT_TIME);
		this.site.getWaitGamePlayerQueue().put(player);
	}
	
	
	
	
	/**
	 * 是否是同一状态
	 * */
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
		this.site.getWaitGamePlayerQueue().remove(player);
		
		if (state == TableState.CALL) {
			// 不叫
			// 叫牌结束切换状态
			if (isSameState(PlayerState.WAIT)) {
				callToDoubles();
			}
			
		} else if (state == TableState.DOUBLES) {
			// 不加倍
			// 加倍结束切换状态
			if (isSameState(PlayerState.WAIT)) {
				doublesToDraw();
			}
		} else if (state == TableState.DRAW) {
			// 过牌
			// 处理下家
			GamePlayer nextPlayer =  players[player.getIndex() + 1 % 4];
			nextPlayer.setState(PlayerState.OPERATE);
			nextPlayer.setOpTime(TimeUtil.getCurrTime() + OPERATE_WAIT_TIME);
			this.site.getWaitGamePlayerQueue().put(nextPlayer);
		}
		// TODO 通知
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
		if (callType == CallType.LIGHT) {
			// 牌桌
			this.callCard = card;
			this.callType = callType;
			this.callOp = player.getIndex();
			this.state = TableState.DOUBLES;
			// 当前玩家
			player.setTeam(Team.RED);
			player.setState(PlayerState.OPERATE);
			player.setOpTime(TimeUtil.getCurrTime() + OPERATE_WAIT_TIME);
			site.getWaitGamePlayerQueue().put(player);
			// 其它玩家
			for (GamePlayer gamePlayer : players) {
				if (gamePlayer.equals(player))
					continue;
				gamePlayer.setTeam(Team.BLUE);
				gamePlayer.setState(PlayerState.WAIT);
				site.getWaitGamePlayerQueue().remove(gamePlayer);
			}
		} else {
			// 只有庄家可以叫牌
			if (player.getIndex() != this.dealer) {
				logger.error("{} Player is not dealer {} {}", player.getId(), player.getIndex(), this.dealer);
				return;
			}
			// 牌桌
			this.callCard = card;
			this.callType = callType;
			this.callOp = player.getIndex();
			// 当前玩家
			player.setTeam(Team.RED);
			player.setState(PlayerState.WAIT);
			this.site.getWaitGamePlayerQueue().remove(player);
			// 全部操作完
			if (isSameState(PlayerState.WAIT)) {
				callToDoubles();
			}
			
		}
		// TODO 通知
		
	}
	/**
	 * 叫牌切换到加倍状态
	 * */
	private void callToDoubles() {
		// 没人叫牌则重新开始
		if (this.callOp == null) {
			deal();
		} else {
			this.state = TableState.DOUBLES;
			for (GamePlayer otherPlayer : this.players) {
				otherPlayer.setState(PlayerState.OPERATE);
			}
			this.waitTime = TimeUtil.getCurrTime() + CALL_WAIT_TIME;
			this.site.getWaitTableQueue().put(this);
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
		for (int i = 0; i < indexs.length ; i ++) {
			Card card = player.getHandCards()[indexs[i]];
			if (card == null) {
				logger.error("{} Indexs not match handCards {] {}", player.getId(), Arrays.toString(indexs), Arrays.toString(player.getHandCards()));
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
			logger.error("{} Cards not BG. lastCards {} {}", player.getId(), Arrays.toString(cards), Arrays.toString(this.lastCards));
			return;
		}
		// 倍数
		MultipleType multiple = MultipleUtil.getMultiple(cardType, cards);
		if (multiple != null) {
			this.multiples[multiple.ordinal()] += multiple.getValue();
		}
		// 得分
		if (this.lastOp == player.getIndex() && this.roundScore != 0) {
			player.setScore(player.getScore() + this.roundScore);
			this.roundScore = 0;
		}
		// 队伍
		if (player.getTeam() == null && this.callType != CallType.LIGHT) {
			for (Card card : cards) {
				if (card.equals(this.callCard)) {
					GamePlayer callPlayer = this.players[this.callOp];
					player.setTeam(callPlayer.getTeam());
					
					for (GamePlayer otherPlayer : this.players) {
						if (otherPlayer.equals(player) || otherPlayer.equals(callPlayer))
							continue;
						otherPlayer.setTeam(Team.BLUE);
					}
				}
			}
		}
		// 出牌后
		boolean isFinish = true;
		for (Card card : player.getHandCards()) {
			if (card != null) {
				isFinish =false;
				break;
			}
		}
		if (isFinish) {
			player.setState(PlayerState.FINISH);
			if (player.getTeam() == null)
				player.setTeam(Team.BLUE);
			// 结算
			if (this.callType == CallType.LIGHT || this.callType == CallType.DARK) {
				if (this.callOp == player.getIndex()) {
					// 其它玩家
					for (GamePlayer otherPlayer : this.players) {
						if (player.equals(otherPlayer))
							continue;
						if (player.getTeam() == null)
							player.setTeam(Team.BLUE);
						otherPlayer.setState(PlayerState.FINISH);
						// 扣除资源
						// 排名
						for (int i = 0; i < ranks.length; i ++) {
							if (ranks[i] == null){
								ranks[i] = player.getIndex();
								break;
							}
						}
						
						
					}
					// 当前玩家添加资源
					// 牌桌
					this.state = TableState.CLOSE;
					// TODO 通知
					
				} else {
					// 当前玩家扣除资源
					// 其它玩家
					for (GamePlayer otherPlayer : this.players) {
						if (player.equals(otherPlayer))
							continue;
						if (player.getTeam() == null)
							player.setTeam(Team.BLUE);
						otherPlayer.setState(PlayerState.FINISH);
						// 添加资源
						// 排名
						for (int i = 0; i < ranks.length; i ++) {
							if (ranks[i] == null){
								ranks[i] = player.getIndex();
								break;
							}
						}
					}
					// 牌桌
					this.state = TableState.CLOSE;
					// TODO 通知
				}
			}
			// 排名
			for (int i = 0; i < ranks.length; i ++) {
				if (ranks[i] == null){
					ranks[i] = player.getIndex();
					break;
				}
			}
			// 
			
		}
		
		// 牌桌
		this.lastCards = cards;
		this.lastCardType = cardType;
		this.lastOp = player.getIndex();
		// 自己
		player.setState(PlayerState.WAIT);
		this.site.getWaitGamePlayerQueue().remove(player);
		// 下家
		
		nextPlayer.setState(PlayerState.OPERATE);
		nextPlayer.setOpTime(TimeUtil.getCurrTime() + OPERATE_WAIT_TIME);
		this.site.getWaitGamePlayerQueue().put(nextPlayer);
		// TODO 通知
	}
	
	
	
	/**
	 * 获取下家
	 * */
	private GamePlayer getNextPlayer(GamePlayer player) {
		GamePlayer nextPlayer =  null;
		if (player.getState() == PlayerState.FINISH) {
			for (GamePlayer otherPlayer : this.players) {
				if (player.equals(otherPlayer))
					continue;
				if (player.getTeam() == otherPlayer.getTeam()){
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
	 * */
	private int addToRanks(GamePlayer player) {
		int rank = -1;
		for (int i = 0; i < this.ranks.length; i ++) {
			if ( this.ranks[i] == null){
				 this.ranks[i] = player.getIndex();
				rank = i;
				break;
			}
		}
		return rank;
	}
	
	/**
	 * 排行数量
	 * */
	private int getRanksCount(GamePlayer player) {
		int count = 0;
		for (int i = 0; i < this.ranks.length; i ++) {
			if ( this.ranks[i] != null)
				count ++;
			else 
				break;
		}
		return count;
	}
	
	/**
	 * 是否完成
	 * */
	private boolean isFinish(GamePlayer player) {
		for (Card card : player.getHandCards()) {
			if (card != null) 
				return false;
		}
		return true;
	}
	
	/**
	 * 玩家出牌后处理
	 * */
	private void doDrawAfter(GamePlayer player) {
		
	}
	
	/**
	 * 处理玩家出牌完成
	 * */
	private void doDrawFinish(GamePlayer player) {

		player.setState(PlayerState.FINISH);
		if (player.getTeam() == null)
			player.setTeam(Team.BLUE);
		// 结算
		if (this.callType == CallType.LIGHT || this.callType == CallType.DARK) {
			if (this.callOp == player.getIndex()) {
				// 其它玩家
				for (GamePlayer otherPlayer : this.players) {
					if (player.equals(otherPlayer))
						continue;
					if (player.getTeam() == null)
						player.setTeam(Team.BLUE);
					otherPlayer.setState(PlayerState.FINISH);
					// 扣除资源
					// 排名
					for (int i = 0; i < ranks.length; i ++) {
						if (ranks[i] == null){
							ranks[i] = player.getIndex();
							break;
						}
					}
					
					
				}
				// 当前玩家添加资源
				// 牌桌
				this.state = TableState.CLOSE;
				// TODO 通知
				
			} else {
				// 当前玩家扣除资源
				// 其它玩家
				for (GamePlayer otherPlayer : this.players) {
					if (player.equals(otherPlayer))
						continue;
					if (player.getTeam() == null)
						player.setTeam(Team.BLUE);
					otherPlayer.setState(PlayerState.FINISH);
					// 添加资源
					// 排名
					for (int i = 0; i < ranks.length; i ++) {
						if (ranks[i] == null){
							ranks[i] = player.getIndex();
							break;
						}
					}
				}
				// 牌桌
				this.state = TableState.CLOSE;
				// TODO 通知
			}
		}
		// 排名
		for (int i = 0; i < ranks.length; i ++) {
			if (ranks[i] == null){
				ranks[i] = player.getIndex();
				break;
			}
		}
		// 
		
	
	}
	
	/**
	 * 结算
	 * */
	private void close(GamePlayer[] winneres) {
		// 当前玩家扣除资源
		// 其它玩家
		for (GamePlayer otherPlayer : this.players) {
			if (player.equals(otherPlayer))
				continue;
			if (player.getTeam() == null)
				player.setTeam(Team.BLUE);
			otherPlayer.setState(PlayerState.FINISH);
			// 添加资源
			// 排名
			for (int i = 0; i < ranks.length; i ++) {
				if (ranks[i] == null){
					ranks[i] = player.getIndex();
					break;
				}
			}
		}
		// 牌桌
		this.state = TableState.CLOSE;
		// TODO 通知
	}
	
	/**
	 * 退出
	 */
	public void exit(GamePlayer player) {
		// 自已退出清空数据
		player.clean();
		// 其他玩家重新设置状态加入匹配
		for (GamePlayer otherPlayer : this.players) {
			if (player.equals(otherPlayer))
				continue;
			otherPlayer.clean();
			this.site.getPlayerDeque().offerFirst(otherPlayer);
		}
		// 牌桌
		clean();
		this.site.getWaitTableQueue().remove(this);
		this.site.getTableQueue().offer(this);
	}

	/**
	 * 准备
	 */
	public void ready(GamePlayer player) {
		player.setState(PlayerState.READY);
		// 全部准备，开始游戏
		for (GamePlayer otherPlayer : this.players) {
			if (otherPlayer.getState() != PlayerState.READY) {
				return;
			}
		}
		// 重置牌桌状态、发牌
		reset();
		deal();
	}

	/**
	 * 重连
	 */
	public void reconnect(PlayerContext playerContext) {
		if (playerContext.getGamePlayer().getTable() == null) 
			return;
		playerContext.getGamePlayer().setAuto(false);
		// TODO 通知
		
	}

	/**
	 * 玩家超时
	 */
	public void checkWaiting(GamePlayer player) {
		
	}

	/**
	 * 等待超时
	 */
	public void checkWaiting() {
		// 加倍超时
		
		// 结算超时
		
	}
	
	public Card[] getCards() {
		return cards;
	}



	public void setCards(Card[] cards) {
		this.cards = cards;
	}



	public GamePlayer[] getPlayers() {
		return players;
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



	public TableState getState() {
		return state;
	}


	/**
	 * 设置牌桌状态
	 * */
	public void setState(TableState state) {
		this.state = state;
	}



	public int getRoundScore() {
		return roundScore;
	}



	public void setRoundScore(int roundScore) {
		this.roundScore = roundScore;
	}



	public Card getCallCard() {
		return callCard;
	}



	public void setCallCard(Card callCard) {
		this.callCard = callCard;
	}


	public Card[] getLastCards() {
		return lastCards;
	}



	public void setLastCards(Card[] lastCards) {
		this.lastCards = lastCards;
	}



	public CardType getLastCardType() {
		return lastCardType;
	}



	public void setLastCardType(CardType lastCardType) {
		this.lastCardType = lastCardType;
	}



	public int getLastOp() {
		return lastOp;
	}



	public void setLastOp(int lastOp) {
		this.lastOp = lastOp;
	}



	public int getDealer() {
		return dealer;
	}



	public void setDealer(int dealer) {
		this.dealer = dealer;
	}



	public long getOpTime() {
		return waitTime;
	}



	public void setOpTime(long opTime) {
		this.waitTime = opTime;
	}



	public Site getSite() {
		return site;
	}



	public void setSite(Site site) {
		this.site = site;
	}



	public TableActor getActor() {
		return actor;
	}



	public void setTableActor(TableActor tableActor) {
		this.actor = tableActor;
	}



	public AkkaContext getAkkaContext() {
		return akkaContext;
	}



	public void setAkkaContext(AkkaContext akkaContext) {
		this.akkaContext = akkaContext;
	}


	@Override
	public int compareTo(Delayed o) {
		if(this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)) {
            return 1;
        }else if(this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS)) {
            return -1;
        }
        return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		 return unit.convert(waitTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

}
