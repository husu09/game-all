package com.su.core.gambling.enums;

public enum PlayerState {
	/**
	 * 匹配中 1
	 * */
	MATCH(1),
	/**
	 * 准备 2
	 */
	READY(2),
	/**
	 * 等待 3
	 */
	WAIT(3),
	/**
	 * 操作中 4
	 */
	OPERATE(4),
	/**
	 * 出完牌 5
	 */
	FINISH(5);
	private int value;
	
	PlayerState(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
