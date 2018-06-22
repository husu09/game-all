package com.su.core.gambling;

import com.su.core.context.PlayerContext;

public interface IMatch {
	/**
	 * 添加玩家到匹配队列
	 */
	public void addPlayerToMatch(PlayerContext playerContext, boolean isFirst);
	/**
	 * 从匹配队列删除玩家
	 */
	public void removePlayerFromMatch(GamePlayer gamePlayer);
}
