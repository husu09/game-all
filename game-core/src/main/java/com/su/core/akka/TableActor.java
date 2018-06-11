package com.su.core.akka;

import com.su.core.gambling.GamePlayer;

public interface TableActor {
	
	/**
	 * 设置
	 * */
	public void setPlayers(GamePlayer[] players);
	/**
	 * 发牌
	 */
	public void deal();
	
	/**
	 * 加倍
	 * */
	public boolean doubles(GamePlayer player, int multiple);
	
	/**
	 * 叫牌
	 */
	public void call(GamePlayer player, int callType, int index);

	/**
	 * 出牌
	 */
	public void draw(GamePlayer player, int cardType, int[] indexs);

	/**
	 * 过牌
	 */
	public void check(GamePlayer player);
	
	/**
	 * 托管
	 * */
	public void setIsAuto(GamePlayer player, int isAuto);

	/**
	 * 准备
	 */
	public void ready(GamePlayer player);
	
	/**
	 * 退出
	 */
	public void exit(GamePlayer player);
	
	/**
	 * 重连
	 * */
	public void reconn(GamePlayer player);
	
	/**
	 * 牌桌超时
	 */
	public void doWaitTable();
	
	/**
	 * 玩家超时
	 */
	public void doWaitGamePlayer(GamePlayer player);

	

}
