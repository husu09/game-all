package com.su.server.obj.play;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.google.protobuf.MessageLiteOrBuilder;
import com.su.common.util.CommonUtils;
import com.su.common.util.SpringUtil;
import com.su.core.akka.AkkaContext;
import com.su.proto.PlayProto.CardPro;
import com.su.proto.PlayProto.GamePlayerPro;
import com.su.proto.PlayProto.GameStartNotice;
import com.su.proto.PlayProto.MultiplePro;
import com.su.proto.PlayProto.TablePro;
import com.su.proto.PlayProto.UpdateGamePlayerNotice;
import com.su.proto.PlayProto.UpdateTableNotice;
import com.su.server.constant.ErrCode;
import com.su.server.context.PlayerContext;

/**
 * 牌桌对象
 */
public class Table implements Delayed {
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
	 * 等待时间
	 */
	private int waitingTime;

	private AkkaContext akkaContext = SpringUtil.getContext().getBean(AkkaContext.class);

	private GameStartNotice.Builder gameStartNotice = GameStartNotice.newBuilder();
	private TablePro.Builder tablePro = TablePro.newBuilder();
	private MultiplePro.Builder multiplePro = MultiplePro.newBuilder();
	private CardPro.Builder cardPro = CardPro.newBuilder();
	private GamePlayerPro.Builder gamePlayerPro = GamePlayerPro.newBuilder();
	private UpdateGamePlayerNotice.Builder updateGamePlayerNotice = UpdateGamePlayerNotice.newBuilder();
	private UpdateTableNotice.Builder updateTableNotice = UpdateTableNotice.newBuilder();
	
	
	@Override
	public int compareTo(Delayed o) {
		if (this.getDelay(TimeUnit.SECONDS) > o.getDelay(TimeUnit.SECONDS))
			return 1;
		else if (this.getDelay(TimeUnit.SECONDS) < o.getDelay(TimeUnit.SECONDS))
			return -1;
		return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(waitingTime, TimeUnit.SECONDS);
	}

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
		for (int i = 0; i < players.length; i++) {
			players[i].setIndex(i);
			players[i].setTable(this);
		}
		start();
	}

	/**
	 * 开始游戏
	 */
	public void start() {
		// 设置牌桌状态
		this.state = TableState.START;
		// 初始倍数
		multiples[MultipleType.CHU_SHI.ordinal()] = MultipleType.CHU_SHI.getValue();
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
		// 加入到超时队列
		site.getDeadLineQueue().offer(players[hold]);
		// 通知
		// 倍数
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
		tablePro.clear();
		gameStartNotice.clear();

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
	 * 过牌
	 */
	public void check(GamePlayer player) {
		site.getDeadLineQueue().remove(player);
		player.setState(PlayerState.WATCH);
		player.setDeadLine(0);
		GamePlayer nextPlayer = players[(player.getIndex() + 1 + 1) % 4 - 1];
		nextPlayer.setState(PlayerState.OPERATING);
		nextPlayer.setDeadLine(30);
		site.getDeadLineQueue().offer(nextPlayer);
		// 通知
		gamePlayerPro.setState(player.getState().ordinal());
		gamePlayerPro.setDeadline(player.getDeadLine());
		updateGamePlayerNotice.addGamePlayer(gamePlayerPro);
		gamePlayerPro.clear();

		gamePlayerPro.setState(nextPlayer.getState().ordinal());
		gamePlayerPro.setDeadline(nextPlayer.getDeadLine());
		updateGamePlayerNotice.addGamePlayer(gamePlayerPro);
		gamePlayerPro.clear();

		noticePlayers(updateGamePlayerNotice);
		updateGamePlayerNotice.clear();

	}

	/**
	 * 叫牌
	 */
	public void call(GamePlayer player, int index) {
		site.getDeadLineQueue().remove(player);
		// 玩家状态处理
		player.setState(PlayerState.WATCH);
		player.setDeadLine(0);
		GamePlayer nextPlayer = players[(player.getIndex() + 1 + 1) % 4 - 1];
		nextPlayer.setState(PlayerState.OPERATING);
		nextPlayer.setDeadLine(30);
		site.getDeadLineQueue().offer(nextPlayer);
		
		if (player.getState() != PlayerState.OPERATING) {
			player.getPlayerContext().sendError(ErrCode.PLAYER_NOT_OPERATING);
			return;
		}
		if (callState != null) {
			player.getPlayerContext().sendError(ErrCode.OTHER_PLAYER_CALLED);
			return;
		}
		// 明叫
		if (index == -1) {
			callState = CallState.LIGHT;
			multiples[MultipleType.MING_JIAO.ordinal()] += MultipleType.MING_JIAO.getValue();
			multiplePro.setType(MultipleType.MING_JIAO.ordinal());
			multiplePro.setValue(multiples[MultipleType.MING_JIAO.ordinal()]);
			tablePro.addMultiples(multiplePro);
			multiplePro.clear();
		} else {
			if (index < 0 || index >= player.getHandCards().length) {
				player.getPlayerContext().sendError(ErrCode.PARAMETER_ERROR);
				return;
			}
			Card card = player.getHandCards()[index];
			if (card == null) {
				player.getPlayerContext().sendError(ErrCode.SYSTEM_ERROR);
				return;
			}
			calledCard = card;
			callState = CallState.CALL;
			multiples[MultipleType.JIAO_PAI.ordinal()] += MultipleType.MING_JIAO.getValue();
		}
		
		// 通知
		gamePlayerPro.setState(player.getState().ordinal());
		gamePlayerPro.setDeadline(player.getDeadLine());
		updateGamePlayerNotice.addGamePlayer(gamePlayerPro);
		gamePlayerPro.clear();

		gamePlayerPro.setState(nextPlayer.getState().ordinal());
		gamePlayerPro.setDeadline(nextPlayer.getDeadLine());
		updateGamePlayerNotice.addGamePlayer(gamePlayerPro);
		gamePlayerPro.clear();

		noticePlayers(updateGamePlayerNotice);
		updateGamePlayerNotice.clear();
		
		updateTableNotice.setTable(tablePro);
		noticePlayers(updateTableNotice);
		updateTableNotice.clear();

	}

	/**
	 * 出牌
	 */
	public void draw(GamePlayer player, CardType cardType, int[] index) {
		
	}

	/**
	 * 退出
	 */
	public void exit(GamePlayer player) {

	}

	/**
	 * 准备
	 */
	public void ready(GamePlayer player) {

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
		if (player.getState() == PlayerState.OPERATING)
			check(player);
	}

	/**
	 * 等待超时
	 */
	public void checkWaiting() {

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

	public Site getSite() {
		return site;
	}

}
