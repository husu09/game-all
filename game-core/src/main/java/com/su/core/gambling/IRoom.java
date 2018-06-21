package com.su.core.gambling;

import java.util.concurrent.DelayQueue;

/**
 * 游戏场接口
 * */
public interface IRoom {
	/**
	 * 牌桌等待队列
	 * */
	public DelayQueue<ITable> getWaitTableQueue();
	/**
	 * 玩家等待队列
	 * */
	public DelayQueue<GamePlayer> getWaitGamePlayerQueue();
}
