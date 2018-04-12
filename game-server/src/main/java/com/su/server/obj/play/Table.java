package com.su.server.obj.play;

/**
 * 牌桌对象
 * */
public class Table {
	/**
	 * 玩家
	 * */
	private GamePlayer[] players;
	/**
	 * 牌
	 * */
	private Card[] cards = new Card[108];
	
	
	
	public Table() {
		for (int i = 0; i < 54; i ++) {
			
		}
	}
	
	
	
	/**
	 * 开始游戏
	 * */
	public void start(GamePlayer[] players) {
		this.players = players;
		
		
	}
	
	/**
	 * 出牌
	 * */
	
	
	/**
	 * 过牌
	 * */
}
