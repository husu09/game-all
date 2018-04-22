package com.su.server.obj.play;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.DelayQueue;

import com.google.protobuf.MessageLiteOrBuilder;
import com.su.common.util.CommonUtils;
import com.su.common.util.SpringUtil;
import com.su.core.akka.AkkaContext;
import com.su.proto.PlayProto.CardPro;
import com.su.proto.PlayProto.GamePlayerPro;
import com.su.proto.PlayProto.GameStartNotice;
import com.su.proto.PlayProto.MultiplePro;
import com.su.proto.PlayProto.TablePro;

/**
 * 牌桌对象
 */
public class Table {
	/**
	 * 场所
	 */
	private Site site;
	/**
	 * 牌
	 */
	private Card[] cards = new Card[108];
	/**
	 * 玩家
	 */
	private GamePlayer[] players;
	/**
	 * 状态
	 */
	private TableState state = TableState.IDLE;
	/**
	 * 牌权
	 */
	private int hold = 0;
	/**
	 * 轮分
	 */
	private int roundScore;
	/**
	 * 被叫的牌
	 */
	private Card calledCard;
	/**
	 * 叫牌状态
	 */
	private CallState callState;
	/**
	 * 倍数
	 */
	private int[] multiples = new int[MultipleType.values().length - 2];
	/**
	 * 最后出牌
	 */
	private Card[] lastCards;
	/**
	 * 庄家
	 */
	private int dealer;
	/**
	 * 操作接口
	 */
	private TableActor actor;
	/**
	 * 操作时间队列
	 */
	private DelayQueue<GamePlayer> deadLineQueue = new DelayQueue<>();
	
	private TablePro.Builder tablePro = TablePro.newBuilder();

	private AkkaContext akkaContext = SpringUtil.getContext().getBean(AkkaContext.class);

	public Table(Site site) {
		this.site = site;
		this.actor = akkaContext.createActor(TableActor.class, TableActorImpl.class);
		this.actor.initActor(this);
		// 生成牌
		for (int j = 0; j < 2; j++) {
			int index = 0;
			int value = 3;
			int suit = 1;
			for (int i = 1; i <= 52; i++) {
				cards[index] = new Card(value, Suit.values()[suit]);
				System.out.println(cards[index]);
				if (i % 4 == 0) {
					value++;
					suit = 1;
				} else {
					suit++;
				}
				index++;
			}
			cards[index] = new Card(value, Suit.values()[0]);
			index++;
			value++;
			cards[index] = new Card(value, Suit.values()[0]);
		}

	}

	/**
	 * 初始化
	 */
	public void init(GamePlayer[] players) {
		this.players = players;
		// 初始倍数
		multiples[MultipleType.CHU_SHI.ordinal()] = MultipleType.CHU_SHI.getValue();
		for (int i = 0; i < players.length; i++) {
			players[i].setIndex(i);
		}
		start();
	}

	/**
	 * 开始游戏
	 */
	public void start() {
		//
		this.state = TableState.START;
		dealer = hold;
		for (int i = 0; i < players.length; i++) {
			if (i == hold)
				continue;
			players[i].setState(PlayerState.WATCH);
		}
		shuffle();
		deal();
		players[hold].setState(PlayerState.OPERATING);
		players[hold].setDeadLine(15);
		deadLineQueue.offer(players[hold]);
		site.getPlayingTableQueue().offer(this);
		// 通知
		GameStartNotice.Builder gameStartNotice = GameStartNotice.newBuilder();
		// 倍数
		MultiplePro.Builder multiplePro = MultiplePro.newBuilder();
		for (MultipleType multipleType : MultipleType.values()) {
			if (multiples[multipleType.ordinal()] != 0 && multipleType.ordinal() < multiples.length) {
				multiplePro.setType(multipleType.ordinal());
				multiplePro.setValue(multiples[multipleType.ordinal()]);
				tablePro.addMultiples(multiplePro);
				multiplePro.clear();
			}

		}
		tablePro.setState(state.ordinal());
		tablePro.setHold(hold);
		tablePro.setRoundScore(roundScore);
		CardPro.Builder cardPro = CardPro.newBuilder();
		if (calledCard != null) {
			cardPro.setValue(calledCard.getValue());
			cardPro.setSuit(calledCard.getSuit().ordinal());
			tablePro.setCalledCard(cardPro);
			cardPro.clear();
		}
		if (lastCards != null) {
			for (Card card : lastCards) {
				cardPro.setValue(card.getValue());
				cardPro.setSuit(card.getSuit().ordinal());
				tablePro.addLastCards(cardPro);
				cardPro.clear();
			}
		}
		tablePro.setDealer(dealer);
		// 玩家数据
		for (int i = 0; i < players.length; i++) {
			for (int j = 0; j < players.length; j++) {
				gamePlayerPro.setId(players[j].getId());
				if (players[i] == players[j]) {
					for (Card card : players[j].getHandCards()) {
						if (card == null)
							continue;
						cardPro.setValue(card.getValue());
						cardPro.setSuit(card.getSuit().ordinal());
						gamePlayerPro.addHandCards(cardPro);
						cardPro.clear();
					}
				}
				int cardNum = 0;
				for (Card card : players[j].getHandCards()) {
					if (card == null)
						break;
					cardNum++;
				}
				gamePlayerPro.setCardNum(cardNum);
				gamePlayerPro.setTeam(players[j].getTeam().ordinal());
				gamePlayerPro.setMyMultipleValue(players[j].getMyScore());
				gamePlayerPro.setMyScore(players[j].getState().ordinal());
				gamePlayerPro.setDeadline(players[j].getDeadLine());
				gamePlayerPro.setIsAuto(players[j].getIsAuto());
				tablePro.addPlayers(gamePlayerPro);
				gamePlayerPro.clear();

			}
			players[i].getPlayerContext().write(gameStartNotice);
			tablePro.clearPlayers();
		}

	}

	/**
	 * 洗牌
	 */
	private void shuffle() {
		for (int i = 0; i < cards.length; i++) {
			Card tmp = cards[i];
			int r = CommonUtils.range(i, cards.length);
			cards[i] = cards[r];
			cards[r] = tmp;
		}
	}

	/**
	 * 发牌
	 */
	private void deal() {
		int index = 0;
		for (int i = 1; i <= cards.length; i++) {
			players[(hold + i) % 4 - 1].getHandCards()[index] = cards[i];
			if (i % 4 == 0)
				index++;
			
		}
		for (int i = 0; i < players.length; i++) {
			Arrays.sort(players[i].getHandCards());
		}
	}

	/**
	 * 处理超时
	 */
	public void checkDeadLine() {
		GamePlayer player = deadLineQueue.poll();
		if (player != null) {
			if (player.getState() == PlayerState.OPERATING)
				player.check();
		}
	}

	/**
	 * 处理过牌
	 */
	public void doCheck(GamePlayer gamePlayer) {
		GamePlayer nextPlayer = players[(gamePlayer.getIndex() + 1 + 1) % 4 - 1];
		nextPlayer.setState(PlayerState.OPERATING);
	}

	/**
	 * 通知所有玩家数据更新
	 */
	private void noticePlayers(MessageLiteOrBuilder msg) {
		for (GamePlayer player : players) {
			player.getPlayerContext().write(msg);
		}
	}

	public TableActor getActor() {
		return actor;
	}

}
