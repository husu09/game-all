package com.su.core.game;

public class ContestRanking {
	/**
	 * 比赛积分
	 * */
	private int contestScore;
	/**
	 * 是否淘汰
	 * */
	private boolean isOut;
	
	public int getContestScore() {
		return contestScore;
	}
	public void setContestScore(int contestScore) {
		this.contestScore = contestScore;
	}
	public boolean isOut() {
		return isOut;
	}
	public void setOut(boolean isOut) {
		this.isOut = isOut;
	}
}
