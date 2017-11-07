package com.su.common.mq;
/**
 * 数据库操作
 * */
public enum MQOperator {
	ADD(1),
	UPDATE(2),
	DELETE(3);
	private int value;

	private MQOperator(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	
}
