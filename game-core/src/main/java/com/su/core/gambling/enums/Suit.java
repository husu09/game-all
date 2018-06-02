package com.su.core.gambling.enums;

public enum Suit {
	/**
	 * 方块 1
	 */
	FANG_KUAI(1),
	/**
	 * 梅花 2
	 */
	MEI_HUA(2),
	/**
	 * 红桃 3
	 */
	HONG_TAO(3),
	/**
	 * 黑桃 4
	 */
	HEI_TAO(4);
	
	private int value;
	
	Suit(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	
}
