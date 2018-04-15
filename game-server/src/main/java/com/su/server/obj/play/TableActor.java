package com.su.server.obj.play;

public interface TableActor {
	/**
	 * 开始
	 * */
	public void start(GamePlayer[] players);
	/**
	 * 叫牌
	 * */
	public void call(GamePlayer player, int index);
	/**
	 * 出牌
	 * */
	public void draw(GamePlayer player,CardType cardType, int[] index);
	/**
	 * 过牌
	 * */
	public void check(GamePlayer player);
	/**
	 * 退出
	 * */
	public void exit(GamePlayer player);
	/**
	 * 准备
	 * */
	public void ready(GamePlayer player);
	/**
	 * 重连
	 * */
	public void reconnect();
	
	
}
