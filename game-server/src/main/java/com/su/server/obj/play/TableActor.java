package com.su.server.obj.play;

import com.su.server.context.PlayerContext;

public interface TableActor {
	/**
	 * 初始化
	 */
	public void initActor(Table table);

	/**
	 * 初始化牌桌
	 */
	public void initTable(GamePlayer[] players);

	/**
	 * 叫牌
	 */
	public void call(GamePlayer player, int index);

	/**
	 * 出牌
	 */
	public void draw(GamePlayer player, CardType cardType, int[] index);

	/**
	 * 过牌
	 */
	public void check(GamePlayer player);

	/**
	 * 退出
	 */
	public void exit(GamePlayer player);

	/**
	 * 准备
	 */
	public void ready(GamePlayer player);

	/**
	 * 重连
	 */
	public void reconnect(PlayerContext playerContext);

	/**
	 * 检测截止时间
	 */
	public void checkDeadline();

}
