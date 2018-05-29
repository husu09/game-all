package com.su.core.akka;

import com.su.core.gambling.GamePlayer;
import com.su.core.gambling.enums.CardType;

public interface TableActor {

	/**
	 * 发牌
	 */
	public void deal();
	
	/**
	 * 加倍
	 * */
	public void doubles(GamePlayer player);
	
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
	 * 准备
	 */
	public void ready(GamePlayer player);
	
	/**
	 * 退出
	 */
	public void exit(GamePlayer player);

}
