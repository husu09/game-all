package com.su.core.gambling;

import java.util.Arrays;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.hibernate.annotations.common.util.impl.Log_.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.MessageLiteOrBuilder;
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
	/**
	 * 被叫的牌的类型
	 * */
	private CallType callType;
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
	
	private Site site;
	
	private TableActor actor;

	AkkaContext akkaContext = SpringUtil.getContext().getBean(AkkaContext.class);
	CardProcessorManager cardManager = SpringUtil.getContext().getBean(CardProcessorManager.class);
	
	private Logger logger = LoggerFactory.getLogger(Table.class);
	
	/**
	 * 叫牌时间
	 * */
	private static final int CALL_WAIT_TIME  = TimeUtil.ONE_SECOND * 15;
	/**
	 * 出版时间
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
	}

	
	/**
	 * 重置牌桌状态
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
	}
	
	/**
	 * 洗牌
	 */
	private void shuffle() {
		for (int i = 0; i < cards.length; i++) {
			Card tmp = cards[i];
			int r = CommonUtil.range(i, cards.length);
			cards[i] = cards[r];
			cards[r] = tmp;
		}
	}

	/**
	 * 发牌
	 */
	public void deal() {
		// 重置
		reset();
		// 洗牌
		shuffle();
		// 发牌
		int index = 0;
		for (int i = 1; i <= this.cards.length; i++) {
			this.players[(this.dealer + i) % 4 - 1].getHandCards()[index] = this.cards[i];
			if (i % 4 == 0)
				index++;
		}
		for (int i = 0; i < this.players.length; i++) {
			Arrays.sort(this.players[i].getHandCards());
		}
		// 设置牌桌状态
		state = TableState.DOUBLES;
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
		if (player.getState() != PlayerState.OPERATE 
				|| state != TableState.DOUBLES)
			return false;
		player.setMultipleValue(player.getMultipleValue() + multiple);
		player.setState(PlayerState.WAIT);
		//TODO 通知
		return true;
	}
	
	/**
	 * 过牌
	 */
	public void check(GamePlayer player) {
		if (player.getState() != PlayerState.OPERATE)
			return;
		if (state == TableState.CALL) {
			// 不叫
			player.setState(PlayerState.WAIT);
		} else if (state == TableState.DOUBLES) {
			// 不加倍
			player.setState(PlayerState.WAIT);
		} else if (state == TableState.DRAW) {
			// 过牌
			// 处理当前玩家
			player.setState(PlayerState.WAIT);
			site.getWaitGamePlayerQueue().remove(player);
			// 处理下家
			GamePlayer nextPlayer =  players[player.getIndex() + 1 % 4];
			nextPlayer.setState(PlayerState.OPERATE);
			nextPlayer.setOpTime(TimeUtil.getCurrTime() + OPERATE_WAIT_TIME);
			site.getWaitGamePlayerQueue().put(nextPlayer);
		}
		// TODO 通知
	}

	/**
	 * 叫牌
	 */
	public void call(GamePlayer player, int callTypeValue, int index) {
		if (player.getState() != PlayerState.OPERATE)
			return;
		CallType callType = CallType.get(callTypeValue);
		Card card = player.getHandCards()[index];
		if (!CallType.verify(callType, player.getHandCards(), card)) {
			return;
		}
		if (callType == CallType.LIGHT) {
			// 牌桌
			this.callCard = card;
			this.callType = callType;
			this.state = TableState.DRAW;
			// 其它玩家
			for (GamePlayer gamePlayer : players) {
				if (gamePlayer.equals(player))
					continue;
				gamePlayer.setState(PlayerState.WAIT);
			}
			// 自己
			player.setState(PlayerState.OPERATE);
			player.setOpTime(TimeUtil.getCurrTime() + OPERATE_WAIT_TIME);
			site.getWaitGamePlayerQueue().put(player);
		} else {
			// 牌桌
			this.callCard = card;
			this.callType = callType;
			player.setState(PlayerState.WAIT);
		}
		// TODO 通知
		
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
		CardType cardType = CardType.get(cardTypeValue);
		if (cardType == null) {
			logger.error("{} CardType is null {}", player.getId(), cardTypeValue);
			return;
		}
		Card[] cards = new Card[indexs.length];
		for (int i = 0; i < indexs.length ; i ++) {
			Card card = player.getHandCards()[indexs[i]];
			if (card == null) {
				logger.error("{} Indexs not match handCards {] {}", player.getId(), Arrays.toString(indexs), Arrays.toString(player.getHandCards()));
				return;
			}
			cards[i] = card;
		}
		
		
		CardProcessor cardProcessor = cardManager.getCardProcessor().get(cardType);
		
		if (!cardProcessor.verify(cards)) {
			logger.error("{} CardType not match Cards {} {}", player.getId(), cardTypeValue, Arrays.toString(indexs));
			return;
		}
		if (!cardProcessor.compare(cards, this.lastCardType, this.lastCards)) {
			logger.error("{} Cards not BG. lastCards {} {}", player.getId(), Arrays.toString(cards), Arrays.toString(lastCards));
			return;
		}
		// 队伍
		if (player.getTeam() == null && this.callType != CallType.LIGHT) {
			for (Card card : cards) {
				if (card.equals(this.callCard)) {
					
				}
			}
		}
		// 牌桌
		this.lastCards = cards;
		this.lastCardType = cardType;
		this.lastOp = player.getIndex();
		// 玩家
		player.setState(state);
		
		
		
	}

	/**
	 * 退出
	 */
	public void exit(GamePlayer player) {}

	/**
	 * 准备
	 */
	public void ready(GamePlayer player) {
		player.setState(PlayerState.READY);
	}

	/**
	 * 重连
	 */
	public void reconnect(PlayerContext playerContext) {

	}

	/**
	 * 玩家超时
	 */
	public void checkDeadLine(GamePlayer player) {
		
	}

	/**
	 * 等待超时
	 */
	public void checkWaiting() {

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
