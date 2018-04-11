package com.su.play;

public class GamePlayer {
	private int score;
	private int ranking;
	private Card[] handCards;
	private Team team;
	private Table table;
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
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
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}
	
	
	
	
	
}
