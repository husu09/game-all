package com.su.core.gambling.enums;

import java.util.HashMap;
import java.util.Map;

import com.su.core.gambling.Card;



public enum CallType {
	/**
	 * 叫牌 1
	 */
	CALL(1),
	/**
	 * 暗叫 2
	 */
	DARK(2),
	/**
	 * 明叫 3
	 */
	LIGHT(3);

	private int value;

	CallType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;

	}
	
	private static final Map<Integer, CallType> map = new HashMap<>(CallType.values().length);
	
	static {
		for (CallType callType : CallType.values()) {
			map.put(callType.getValue(), callType);
		}
	}
	
	/**
	 * 根据值获取Enum
	 */
	public static CallType get(int value) {
		return map.get(value);
	}

	/**
	 * 叫牌验证
	 */
	public static boolean verify(CallType callType, Card[] cards, Card card) {
		boolean isSuccess = false;
		switch (callType) {
		case CALL:
		case DARK:
			if (card == null)
				return false;
			for (Card c : cards) {
				if (c.equals(card)) {
					isSuccess = true;
					break;
				}
			}
			break;
		case LIGHT:
			isSuccess = true;
			break;
		default:
			break;
		}
		return isSuccess;
	}
}
