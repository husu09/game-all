package com.su.core.game;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.su.config.SiteCo;
import com.su.core.context.PlayerContext;

public abstract class MatchSite extends Site {
	/**
	 * 空闲牌桌队列
	 */
	private ConcurrentLinkedQueue<Table> idleTableQueue = new ConcurrentLinkedQueue<>();

	public ConcurrentLinkedQueue<Table> getIdleTableQueue() {
		return idleTableQueue;
	}

	/**
	 * 加入匹配
	 */
	public abstract void addPlayerToMatch(PlayerContext playerContext, boolean isFirst);

	/**
	 * 从匹配队列中删除
	 */
	public abstract void removePlayerFromMatch(GamePlayer gamePlayer);

	/**
	 * 获取配置对象
	 */
	public abstract SiteCo getSiteCo();

}
