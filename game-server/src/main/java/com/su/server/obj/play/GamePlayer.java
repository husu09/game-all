package com.su.server.obj.play;

import com.su.server.context.PlayerContext;

/**
 * 游戏中的玩家对象
 * */
public class GamePlayer {
	/**
	 * 是否托管
	 * */
	private boolean isAuto;
	/**
	 * 手牌
	 * */
	private Card[] handCards;
	/**
	 * 玩家上下文
	 * */
	private PlayerContext playerContext;
}
