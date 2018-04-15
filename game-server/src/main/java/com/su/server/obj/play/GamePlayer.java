package com.su.server.obj.play;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.su.core.context.PlayerContext;

/**
 * 游戏中的玩家对象
 */
public class GamePlayer {
	/**
	 * 手牌
	 */
	private Card[] handCards = new Card[27];
	/**
	 * 队伍
	 */
	private Team team = Team.NONE;
	/**
	 * 我的倍数
	 */
	private MultipleType multipleType;
	private int multiple;
	/**
	 * 分数
	 */
	private int myScore;
	/**
	 * 状态
	 */
	private PlayerState state;
	/**
	 * 出牌时间
	 */
	private long deadLine;
	/**
	 * 是否托管
	 */
	private boolean isAuto;
	/**
	 * 玩家上下文
	 */
	private PlayerContext playerContext;

	public GamePlayer(PlayerContext playerContext) {
		this.playerContext = playerContext;
	}
	
	

}
