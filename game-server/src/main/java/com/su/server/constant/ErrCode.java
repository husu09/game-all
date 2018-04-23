package com.su.server.constant;

public class ErrCode {
	// 系统错误
	public static final int SYSTEM_ERROR = 0002;
	// 没有发现Action
	public static final int NOT_FIND_ACTION = 0003;
	// 没有发现PlayerContext
	public static final int NOT_FIND_PLAYER_CONTEXT = 0004;

	// 玩家姓名为空
	public static final int PLAYER_NAME_IS_EMPTY = 1001;
	// 玩家为空
	public static final int PLAYER_IS_NULL = 1002;
	
	// 玩家不在操作状态
	public static final int PLAYER_NOT_OPERATING = 2001;
	// 不在叫牌状态
	public static final int OTHER_PLAYER_CALLED = 2002;
	// 参数错误
	public static final int PARAMETER_ERROR = 2003;
	// 牌型错误
	public static final int CARD_TYPE_ERROR = 2004;
	// 牌型大小错误
	public static final int CARD_SIZE_ERROR = 2005;
}
