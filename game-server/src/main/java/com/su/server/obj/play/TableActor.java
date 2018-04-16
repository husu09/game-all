package com.su.server.obj.play;

import com.su.server.context.PlayerContext;

public interface TableActor {
	/**
	 * 初始化
	 * */
	public void init(Table table);
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
	public void reconnect(PlayerContext playerContext);
	/**
	 * 检测牌权变更
	 * */
	public void check();
	
	
}
