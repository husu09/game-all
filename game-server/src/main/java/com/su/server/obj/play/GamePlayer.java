package com.su.server.obj.play;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.su.core.context.PlayerContext;
import com.su.msg.PlayMsg.PCard;
import com.su.msg.PlayMsg.PGamePlayer;

/**
 * 游戏中的玩家对象
 */
public class GamePlayer implements Delayed {
	private long id;
	/**
	 * 坐位
	 */
	private int index;
	/**
	 * 手牌
	 */
	private Card[] handCards;
	/**
	 * 队伍
	 */
	private Team team;
	/**
	 * 倍数
	 */
	private int multipleValue;
	/**
	 * 分数
	 */
	private int score;
	/**
	 * 状态
	 */
	private PlayerState state;
	/**
	 * 截止时间
	 */
	private int deadLine;
	/**
	 * 是否托管
	 */
	private int isAuto;
	/**
	 * 玩家上下文
	 */
	private PlayerContext playerContext;
	/**
	 * 牌桌
	 */
	private Table table;
	/**
	 * 手牌数量
	 */
	private final static int HAND_CARDS_NUM = 27;

	public GamePlayer(PlayerContext playerContext) {
		this.playerContext = playerContext;
		this.id = playerContext.getPlayer().getId();
		handCards = new Card[HAND_CARDS_NUM];
	}

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
		return unit.convert(deadLine, TimeUnit.SECONDS);
	}

	/**
	 * 获取手牌数量
	 */
	public int getHandCardsNum() {
		int num = 0;
		for (Card card : handCards) {
			if (card != null)
				num++;
		}
		return num;
	}

	public PGamePlayer toProto(boolean isContainHandCards) {
		PGamePlayer.Builder builder = PGamePlayer.newBuilder();
		return toProto(builder, isContainHandCards);
	}

	public PGamePlayer toProto(PGamePlayer.Builder builder, boolean isContainHandCards) {
		PCard.Builder pCardProBuilder = PCard.newBuilder();
		builder.setId(id);
		if (isContainHandCards) {
			for (Card card : handCards) {
				if (card == null)
					continue;
				builder.addHandCards(card.toProto(pCardProBuilder));
			}
		}
		builder.setCardNum(getHandCardsNum());
		if (team != null)
			builder.setTeam(team.ordinal());
		builder.setMultipleValue(multipleValue);
		builder.setScore(score);
		if (state != null)
			builder.setState(state.ordinal());
		builder.setDeadline(deadLine);
		builder.setIsAuto(isAuto);
		PGamePlayer pGamePlayer = builder.build();
		builder.clear();
		return pGamePlayer;
	
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Card[] getHandCards() {
		return handCards;
	}

	public void setHandCards(Card[] handCards) {
		this.handCards = handCards;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public int getMultipleValue() {
		return multipleValue;
	}

	public void setMultipleValue(int multipleValue) {
		this.multipleValue = multipleValue;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int myScore) {
		this.score = myScore;
	}

	public PlayerState getState() {
		return state;
	}

	public void setState(PlayerState state) {
		this.state = state;
	}

	public int getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(int deadLine) {
		this.deadLine = deadLine;
	}

	public int getIsAuto() {
		return isAuto;
	}

	public void setIsAuto(int isAuto) {
		this.isAuto = isAuto;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public long getId() {
		return id;
	}

	public PlayerContext getPlayerContext() {
		return playerContext;
	}

}
