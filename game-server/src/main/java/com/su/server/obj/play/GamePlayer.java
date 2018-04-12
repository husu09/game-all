package com.su.server.obj.play;

import com.su.server.context.PlayerContext;

/**
 * 游戏中的玩家对象
 * */
public class GamePlayer {
	/**
	 * 是否托管
	 * */
	private boolean isAuto;
	/**
	 * 手牌
	 * */
	private Card[] handCards = new Card[27];
	/**
	 * 分数
	 * */
	private int score;
	/**
	 * 队伍
	 * */
	private Team team = Team.NONE;
	/**
	 * 玩家上下文
	 * */
	private PlayerContext playerContext;
	
	public GamePlayer(PlayerContext playerContext) {
		this.playerContext = playerContext;
	}

	public boolean isAuto() {
		return isAuto;
	}

	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}

	public Card[] getHandCards() {
		return handCards;
	}

	public void setHandCards(Card[] handCards) {
		this.handCards = handCards;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public PlayerContext getPlayerContext() {
		return playerContext;
	}
	
	
	
}
