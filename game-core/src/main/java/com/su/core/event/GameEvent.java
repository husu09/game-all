package com.su.core.event;

import com.su.core.context.PlayerContext;

public interface GameEvent {
	/**
	 * 服务启动
	 * */
	public void serverStart();
	/**
	 * 服务关闭
	 * */
	public void serverStop();
	/**
	 * 服务数据每日重置
	 * */
	public void dailyReset();
	/**
	 * 登录
	 * */
	public void login(PlayerContext playerContext);
	/**
	 * 登出
	 * */
	public void logout(PlayerContext playerContext);
	/**
	 * 玩家数据每日重置
	 * */
	public void dailyReset(PlayerContext playerContext);
}
