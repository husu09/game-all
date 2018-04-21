package com.su.server.obj.play;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.DelayQueue;

import com.su.common.util.CommonUtils;
import com.su.common.util.SpringUtil;
import com.su.core.akka.AkkaContext;
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
	private Map<Integer, Integer> multiples;
	/**
	 * 最后出牌
	 * */
	private Card[] lastCards;
	/**
	 * 操作接口
	 */
	private TableActor actor;
	/**
	 * 操作时间队列
	 */
	private DelayQueue<GamePlayer> deadLineQueue = new DelayQueue<>();

	private AkkaContext akkaContext = SpringUtil.getContext().getBean(AkkaContext.class);

	public TablePro toProto() {
		TablePro.Builder builder = TablePro.newBuilder();
		GamePlayerPro.Builder gamePlayerProBuilder = GamePlayerPro.newBuilder();
		for (int i = 0; i < players.length; i++) {
			builder.addPlayers(players[i].toProto(gamePlayerProBuilder));
			gamePlayerProBuilder.clear();
		}
		MultiplePro.Builder multipleProBuilder = MultiplePro.newBuilder();
		for (Entry<Integer, Integer> entry : multiples.entrySet()) {
			builder.addMultiples(multipleProBuilder.setType(entry.getKey()).setValue(entry.getValue()));
			multipleProBuilder.clear();
		}
		builder.setState(state.ordinal());
		builder.setHold(hold);
		builder.setRoundScore(roundScore);
		builder.setCalledCard(calledCard.toProto());
		builder.setCallState(callState.ordinal());
		return builder.build();
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
	 * */
	public void init(GamePlayer[] players) {
		this.players = players;
		// 初始倍数
		multiples.put(MultipleType.CHU_SHI.ordinal(), MultipleType.CHU_SHI.getValue());
		for (int i = 0; i < players.length; i++) {
			players[i].setIndex(i);
		}
		shuffle();
		start();
	}

	/**
	 * 开始游戏
	 */
	public void start() {
		this.state = TableState.START;
		shuffle();
		deal();
		players[hold].setState(PlayerState.OPERATING);
		players[hold].setDeadLine(15);
		for (int i = 0; i < players.length; i++) {
			if (i == hold)
				continue;
			players[i].setState(PlayerState.WATCH);
		}
		deadLineQueue.offer(players[hold]);
		site.getPlayingTableQueue().offer(this);
		// 通知
		GameStartNotice.Builder builder = GameStartNotice.newBuilder();
		builder.setTable(toProto());
		for (int i = 0; i < players.length; i++) {
			players[i].getPlayerContext().write(builder);
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
	}

	public void checkDeadLine() {
		GamePlayer player = deadLineQueue.poll();
		if (player != null) {
			if (player.getState() == PlayerState.OPERATING)
				player.check();
		}
	}


	public void doCheck(GamePlayer gamePlayer) {
		GamePlayer nextPlayer = players[(gamePlayer.getIndex() + 1 + 1) % 4 - 1];
		nextPlayer.setState(PlayerState.OPERATING);
		
	}

	public TableActor getActor() {
		return actor;
	}
	
	

}
