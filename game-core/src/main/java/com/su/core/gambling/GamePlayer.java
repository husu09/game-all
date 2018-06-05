package com.su.core.gambling;

import com.su.core.gambling.enums.PlayerState;
import com.su.core.gambling.enums.Team;


/**
 * 游戏中的玩家对象
 */
public class GamePlayer {
	private long id;
	/**
	 * 坐位
	 */
	private int index;
	/**
	 * 手牌
	 */
	private Card[] handCards;
	/**
	 * 队伍
	 */
	private Team team;
	/**
	 * 倍数
	 */
	private int multipleValue;
	/**
	 * 分数
	 */
	private int score;
	/**
	 * 状态
	 */
	private PlayerState state;
	/**
	 * 是否托管
	 */
	private int isAuto;
	/**
	 * 名次
	 * */
	private int rank;
	
}
