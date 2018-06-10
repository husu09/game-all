package com.su.server.obj.play;

import com.su.server.context.PlayerContext;

public interface TableActor {

	/**
	 * 开始游戏
	 */
	public void start(GamePlayer[] players);

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
	 * 玩家超时
	 */
	public void checkDeadline(GamePlayer player);
	
	/**
	 * 等待超时
	 * */
	public void checkWaiting();

	void initTable(GamePlayer[] players);

}
