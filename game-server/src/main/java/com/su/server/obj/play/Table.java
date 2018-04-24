package com.su.server.obj.play;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.MessageOrBuilder;
import com.su.common.util.CommonUtils;
import com.su.common.util.SpringUtil;
import com.su.core.akka.AkkaContext;
import com.su.proto.PlayProto.CardPro;
import com.su.proto.PlayProto.GamePlayerNotice;
import com.su.proto.PlayProto.GamePlayerPro;
import com.su.proto.PlayProto.MultiplePro;
import com.su.proto.PlayProto.PCard;
import com.su.proto.PlayProto.PGamePlayer;
import com.su.proto.PlayProto.PMultiple;
import com.su.proto.PlayProto.PTable;
import com.su.proto.PlayProto.TableNotice;
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
	 * 牌权
	 */
	private int hold;
	/**
	 * 轮分
	 */
	private int roundScore;
	/**
	 * 被叫的牌
	 */
	private Card callCard;
	/**
	 * 叫牌状态
	 */
	private CallState callState;
	/**
	 * 倍数
	 */
	private Multiple[] multiples;
	/**
	 * 最后出牌
	 */
	private Card[] lastCards;
	private CardType lastCardType;
	/**
	 * 庄家
	 */
	private int dealer;
	/**
	 * 名次
	 */
	private Integer[] rank;
	/**
	 * 等待时间
	 */
	private int waitingTime;
	/**
	 * 操作接口
	 */
	private TableActor actor;

	private AkkaContext akkaContext = SpringUtil.getContext().getBean(AkkaContext.class);

	private PTable.Builder pTableBuiler = PTable.newBuilder();
	private PGamePlayer.Builder pGamePlayerBuilder = PGamePlayer.newBuilder();
	private PMultiple.Builder pMultipleBuilder = PMultiple.newBuilder();
	private PCard.Builder pCardBuilder = PCard.newBuilder();
	private TableNotice.Builder tableNoticeBuilder = TableNotice.newBuilder();
	private GamePlayerNotice.Builder gamePlayerNoticeBuilder = GamePlayerNotice.newBuilder();

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

	public PTable toProto() {
		for (GamePlayer player : players) {
			pTableBuiler.addPlayers(player.toProto(pGamePlayerBuilder, false));
		}
		for (Multiple multiple : multiples) {
			pTableBuiler.addMultiples(multiple.toProto(pMultipleBuilder));
		}
		if (state != null)
			pTableBuiler.setState(state.ordinal());
		pTableBuiler.setHold(hold);
		pTableBuiler.setRoundScore(roundScore);
		if (callCard != null)
			pTableBuiler.setCallCard(callCard.toProto(pCardBuilder));
		if (callState != null)
			pTableBuiler.setCallState(callState.ordinal());
		if (lastCards != null) {
			for (Card card : lastCards) {
				pTableBuiler.addLastCards(card.toProto(pCardBuilder));
			}
		}
		pTableBuiler.setDealer(dealer);
		PTable pTable = pTableBuiler.build();
		pTableBuiler.clear();
		return pTable;
	}

	public Table(Site site) {
		this.site = site;
		this.actor = this.akkaContext.createActor(TableActor.class, TableActorImpl.class, this);
		// 卡牌
		Card[] oneCards = Card.getADeckOfCards();
		Card[] cards = new Card[Card.CARDS_NUM * 2];
		System.arraycopy(oneCards, 0, cards, 0, Card.CARDS_NUM);
		System.arraycopy(oneCards, 0, cards, Card.CARDS_NUM, Card.CARDS_NUM);
		this.cards = cards;
	}

	private Multiple addMultiple(MultipleType multipleType) {
		Multiple multiple = multiples[multipleType.ordinal()];
		if (multiple == null) {
			multiples[multipleType.ordinal()] = new Multiple(multipleType.ordinal(), multipleType.getValue());
		} else {
			multiple.setValue(multiple.getValue() + multipleType.getValue());
		}
		return multiple;
	}

	public void start(GamePlayer[] players) {
		// 玩家数据
		this.players = players;
		for (int i = 0; i < players.length; i++) {
			players[i].setIndex(i);
			players[i].setState(PlayerState.WATCH);
			players[i].setTable(this);
		}
		// 牌桌数据
		this.multiples = new Multiple[MultipleType.values().length];
		this.rank = new Integer[this.players.length];
		this.dealer = players[0].getIndex();
		start();
	}

	/**
	 * 开始游戏
	 */
	public void start() {
		// 设置牌桌状态
		this.state = TableState.START;
		// 初始倍数
		addMultiple(MultipleType.CHU_SHI);
		shuffle();
		deal();
		// 牌权
		this.hold = this.dealer;
		this.players[this.hold].setState(PlayerState.OPERATING);
		this.players[this.hold].setDeadLine(15);
		// 加入到超时队列
		this.site.getDeadLineQueue().offer(this.players[this.hold]);
		// 通知
		tableNoticeBuilder.setTable(toProto());
		noticePlayers(tableNoticeBuilder);
		tableNoticeBuilder.clear();

		for (GamePlayer player : players) {
			for (Card card : player.getHandCards()) {
				if (card == null)
					continue;
				pGamePlayerBuilder.addHandCards(card.toProto(pCardBuilder));
			}
			gamePlayerNoticeBuilder.addGamePlayer(pGamePlayerBuilder);
			pGamePlayerBuilder.clear();
			player.getPlayerContext().write(gamePlayerNoticeBuilder);
			gamePlayerNoticeBuilder.clear();
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
		for (int i = 1; i <= this.cards.length; i++) {
			this.players[(this.hold + i) % 4].getHandCards()[index] = this.cards[i];
			if (i % 4 == 0)
				index++;
		}
		for (int i = 0; i < this.players.length; i++) {
			Arrays.sort(this.players[i].getHandCards());
		}
	}

	/**
	 * 过牌
	 */
	public void check(GamePlayer player) {
		if (player.getState() != PlayerState.OPERATING  || state != TableState.PLAYING) {
			player.getPlayerContext().sendError(ErrCode.PLAYER_NOT_OPERATING);
			return;
		}
		site.getDeadLineQueue().remove(player);
		player.setState(PlayerState.WATCH);
		player.setDeadLine(0);
		GamePlayer nextPlayer = players[(player.getIndex() + 1 + 1) % 4 - 1];
		nextPlayer.setState(PlayerState.OPERATING);
		nextPlayer.setDeadLine(30);
		site.getDeadLineQueue().offer(nextPlayer);
		// 通知
		pGamePlayerBuilder.setState(player.getState().ordinal());
		pGamePlayerBuilder.setDeadline(player.getDeadLine());
		updateGamePlayerNotice.addGamePlayer(pGamePlayerBuilder);
		pGamePlayerBuilder.clear();

		pGamePlayerBuilder.setState(nextPlayer.getState().ordinal());
		pGamePlayerBuilder.setDeadline(nextPlayer.getDeadLine());
		updateGamePlayerNotice.addGamePlayer(pGamePlayerBuilder);
		pGamePlayerBuilder.clear();

		noticePlayers(updateGamePlayerNotice);
		updateGamePlayerNotice.clear();

	}

	/**
	 * 叫牌
	 */
	public void call(GamePlayer player, int index) {
		if (player.getState() != PlayerState.OPERATING || state != TableState.START) {
			player.getPlayerContext().sendError(ErrCode.PLAYER_NOT_OPERATING);
			return;
		}
		// 明叫
		if (index == -1) {
			callState = CallState.LIGHT;
			pTableBuiler.setCallState(callState.ordinal());
			Multiple multiple = addMultiple(MultipleType.MING_JIAO);
			pTableBuiler.addMultiples(multiple.toProto(pMultipleBuilder));
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
			callCard = card;
			callState = CallState.CALL;
			pTableBuiler.setCallState(callState.ordinal());
			pTableBuiler.setCallCard(callCard.toProto(pCardBuilder));
			Multiple multiple = addMultiple(MultipleType.JIAO_PAI);
			pTableBuiler.addMultiples(multiple.toProto(pMultipleBuilder));
		}
		state = TableState.PLAYING;
		pTableBuiler.setState(state.ordinal());
		player.setTeam(Team.RED);
		pGamePlayerBuilder.setTeam(player.getTeam().ordinal());

		// 玩家状态处理
		site.getDeadLineQueue().remove(player);
		player.setState(PlayerState.WATCH);
		pGamePlayerBuilder.setState(PlayerState.WATCH.ordinal());
		player.setDeadLine(0);
		pGamePlayerBuilder.setDeadline(player.getDeadLine());
		gamePlayerNoticeBuilder.addGamePlayer(pGamePlayerBuilder);
		pGamePlayerBuilder.clear();

		GamePlayer nextPlayer = players[(player.getIndex() + 1 + 1) % 4 - 1];
		nextPlayer.setState(PlayerState.OPERATING);
		pGamePlayerBuilder.setState(nextPlayer.getState().ordinal());
		nextPlayer.setDeadLine(30);
		
		pGamePlayerBuilder.setDeadline(nextPlayer.getDeadLine());
		gamePlayerNoticeBuilder.addGamePlayer(pGamePlayerBuilder);
		pGamePlayerBuilder.clear();
		site.getDeadLineQueue().offer(nextPlayer);
		
		tableNoticeBuilder.setTable(pTableBuiler);
		noticePlayers(tableNoticeBuilder);
		tableNoticeBuilder.clear();
		
		noticePlayers(gamePlayerNoticeBuilder);
		gamePlayerNoticeBuilder.clear();
		

	}

	/**
	 * 出牌
	 */
	public void draw(GamePlayer player, CardType cardType, int[] indexs) {
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
		Card[] cards = new Card[indexs.length];
		// 索引验证
		for (int i = 0; i < indexs.length; i++) {
			if (indexs[i] < 0 || indexs[i] >= player.getHandCards().length) {
				player.getPlayerContext().sendError(ErrCode.PARAMETER_ERROR);
				return;
			}
			Card card = player.getHandCards()[indexs[i]];
			if (card == null) {
				player.getPlayerContext().sendError(ErrCode.SYSTEM_ERROR);
				return;
			}
			cards[i] = card;
		}
		// 牌型验证
		if (!CardTypeUnit.verify(cardType, cards)) {
			player.getPlayerContext().sendError(ErrCode.CARD_TYPE_ERROR);
			return;
		}
		// 比较大小
		if (!CardTypeUnit.compare(cardType, cards, lastCardType, lastCards)) {
			player.getPlayerContext().sendError(ErrCode.CARD_SIZE_ERROR);
			return;
		}
		lastCardType = cardType;
		lastCards = cards;

		// 处理玩家手牌
		for (int index : indexs) {
			player.getHandCards()[index] = null;
		}
		// 倍数
		MultipleType multiple = MultipleTypeUnit.getMultiple(cardType, cards);
		if (multiple != null) {
			multiples[multiple.ordinal()] += multiple.getValue();
			pMultipleBuilder.setType(multiple.ordinal()).setValue(multiples[multiple.ordinal()]);
			pTableBuiler.addMultiples(pMultipleBuilder);
			pMultipleBuilder.clear();
		}
		// 变更牌权
		hold = player.getIndex();
		pTableBuiler.setHold(hold);
		// 叫牌状态
		for (Card card : cards) {
			if (card.equals(callCard)) {
				if (dealer == player.getId()) {
					// 暗叫
					if (callState != CallState.LIGHT) {
						callState = CallState.DARK;
					}
				}
				// 分组
				player.setTeam(players[dealer].getTeam());
			}
		}
		// 检测输赢
		boolean isWin = true;
		for (Card card : player.getHandCards()) {
			if (card != null) {
				isWin = false;
				break;
			}
		}
		if (isWin) {

		}
		// 接风
		GamePlayer teammate = null;

	}

	/**
	 * 退出
	 */
	public void exit(GamePlayer player) {
		// 对局中
		if (state == TableState.WAITING) {
			// 解散牌局
		}
		if (player.getState() == PlayerState.OPERATING) {
			player.setIsAuto(1);
			check(player);
		}
	}

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
