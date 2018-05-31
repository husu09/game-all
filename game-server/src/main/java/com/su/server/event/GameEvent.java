package com.su.server.event;

import com.su.server.context.PlayerContext;

public interface GameEvent {
	/**
	 * 服务启动
	 */
	public void serverStart();

	/**
	 * 服务关闭
	 */
	public void serverStop();

	/**
	 * 服务数据每日重置
	 */
	public void dailyReset();

	/**
	 * 玩家数据每日重置
	 */
	public void dailyReset(PlayerContext playerContext);

	/**
	 * 登录
	 */
	public void login(PlayerContext playerContext);

	/**
	 * 登出
	 */
	public void logout(PlayerContext playerContext);
}
