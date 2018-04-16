package com.su.server.obj.play;

import com.su.proto.PlayProto.CardPro;
import com.su.proto.PlayProto.GamePlayerPro;
import com.su.proto.PlayProto.MultiplePro;
import com.su.server.context.PlayerContext;

/**
 * 游戏中的玩家对象
 */
public class GamePlayer {
	/**
	 * 坐位
	 * */
	private int index;
	/**
	 * 手牌
	 */
	private Card[] handCards = new Card[27];
	/**
	 * 队伍
	 */
	private Team team = Team.NONE;
	/**
	 * 我的倍数
	 */
	private int multipleType;
	private int multiple;
	/**
	 * 分数
	 */
	private int myScore;
	/**
	 * 状态
	 */
	private PlayerState state = PlayerState.READY;
	/**
	 * 出牌时间
	 */
	private long deadLine;
	/**
	 * 是否托管
	 */
	private int isAuto;
	/**
	 * 玩家上下文
	 */
	private PlayerContext playerContext;

	public GamePlayer(PlayerContext playerContext) {
		this.playerContext = playerContext;
	}

	public GamePlayerPro toProto() {
		GamePlayerPro.Builder builder = GamePlayerPro.newBuilder();
		return toProto(builder);
	}

	public GamePlayerPro toProto(GamePlayerPro.Builder builder) {
		if (builder == null)
			builder = GamePlayerPro.newBuilder();
		CardPro.Builder cardProBuilder = CardPro.newBuilder();
		for (Card card : handCards) {
			builder.addHandCards(card.toProto(cardProBuilder));
			cardProBuilder.clear();
		}
		builder.setTeam(team.ordinal());
		MultiplePro.Builder multipleProBuilder = MultiplePro.newBuilder();
		multipleProBuilder.setType(multipleType).setValue(multiple);
		builder.setMyMultiple(multipleProBuilder.build());
		builder.setMyScore(myScore);
		builder.setState(state.ordinal());
		builder.setDeadline(deadLine);
		builder.setIsAuto(isAuto);
		return builder.build();
	}

	public PlayerState getState() {
		return state;
	}

	public void setState(PlayerState state) {
		this.state = state;
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

	public int getMultipleType() {
		return multipleType;
	}

	public void setMultipleType(int multipleType) {
		this.multipleType = multipleType;
	}

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public int getMyScore() {
		return myScore;
	}

	public void setMyScore(int myScore) {
		this.myScore = myScore;
	}

	public long getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(long deadLine) {
		this.deadLine = deadLine;
	}

	public int isAuto() {
		return isAuto;
	}

	public void setAuto(int isAuto) {
		this.isAuto = isAuto;
	}

	public PlayerContext getPlayerContext() {
		return playerContext;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
