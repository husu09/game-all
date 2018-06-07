package com.su.core.akka;

import com.su.core.gambling.GamePlayer;
import com.su.core.gambling.enums.CardType;

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
	public void draw(GamePlayer player, CardType cardType, int[] index);

	/**
	 * 过牌
	 */
	public void check(GamePlayer player);

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

}
