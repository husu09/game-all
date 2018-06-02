package com.su.core.gambling.enums;

public enum Team {
	/**
	 * 红 1
	 * */
	RED(1),
	/**
	 * 蓝 2
	 * */
	BLUE(2);
	private int value;
	
	Team(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
