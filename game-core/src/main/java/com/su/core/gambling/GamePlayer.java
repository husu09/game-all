package com.su.core.gambling;

import com.su.core.context.PlayerContext;
import com.su.core.gambling.enums.PlayerState;
import com.su.core.gambling.enums.Team;


/**
 * 游戏中的玩家对象
 */
public class GamePlayer {
	
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
	 * 是否托管
	 */
	private int isAuto;
	/**
	 * 名次
	 * */
	private int rank;
	
	private Table table;
	
	private PlayerContext playerContext;
	
	public GamePlayer(PlayerContext playerContext) {
		this.playerContext = playerContext;
		this.id = playerContext.getPlayerId();
	}
	
	public long getId() {
		return id;
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

	public void setScore(int score) {
		this.score = score;
	}

	public PlayerState getState() {
		return state;
	}

	public void setState(PlayerState state) {
		this.state = state;
	}

	public int getIsAuto() {
		return isAuto;
	}

	public void setIsAuto(int isAuto) {
		this.isAuto = isAuto;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public PlayerContext getPlayerContext() {
		return playerContext;
	}

	
}
