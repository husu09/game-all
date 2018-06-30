package com.su.core.game;

import com.su.config.SiteCo;
import com.su.core.context.PlayerContext;

public interface IMatch {
	/**
	 * 加入匹配
	 */
	public void addPlayerToMatch(PlayerContext playerContext, boolean isFirst);

	/**
	 * 从匹配队列中删除
	 */
	public void removePlayerFromMatch(GamePlayer gamePlayer);

	/**
	 * 获取配置对象
	 */
	public SiteCo getSiteCo();
}
