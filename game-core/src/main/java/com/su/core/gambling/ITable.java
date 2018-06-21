package com.su.core.gambling;

import java.util.concurrent.Delayed;

/**
 * 牌桌接口
 * */
public interface ITable extends Delayed {
	/**
	 * 设置玩家
	 */
	public void setPlayers(GamePlayer[] players);
	/**
	 * 发牌
	 */
	public void deal();
	/**
	 * 叫牌
	 */
	public void call(GamePlayer player, int callTypeValue, int index);
	/**
	 * 加倍
	 */
	public boolean doubles(GamePlayer player, int multiple);
	/**
	 * 过牌
	 * */
	public void check(GamePlayer player);
	/**
	 * 出牌
	 */
	public void draw(GamePlayer player, int cardTypeValue, int[] indexs);
	/**
	 * 结算
	 */
	public void close(); 
	/**
	 * 准备
	 */
	public void ready(GamePlayer player);
	/**
	 * 退出
	 */
	public void exit(GamePlayer gamePlayer);
	/**
	 * 重连
	 */
	public void reconnect(GamePlayer gamePlayer);
	/**
	 * 玩家操作超时
	 */
	public void doWaitGamePlayer(GamePlayer gamePlayer);
	/**
	 * 牌桌等待超时
	 */
	public void doWaitTable();
}
