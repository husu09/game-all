package com.su.core.gambling.service;

import com.su.core.gambling.Card;
import com.su.core.gambling.ITable;
import com.su.core.gambling.enums.MultipleType;

public class MultipleService {
	/**
	 * 增加公共倍数
	 */
	public void addMultiple(ITable table, Object o, Card[] cards) {
		MultipleType multiple = MultipleType.getMultiple(o, cards);
		if (multiple != null) {
			this.multiples[multiple.ordinal()] += multiple.getValue();
		}
	}

	/**
	 * 扣除公共倍数
	 */
	public void eddMultiple(ITable table, Object o, Card[] cards) {
		MultipleType multiple = MultipleType.getMultiple(o, cards);
		if (multiple != null && this.multiples[multiple.ordinal()] >= multiple.getValue()) {
			this.multiples[multiple.ordinal()] -= multiple.getValue();
		}
	}
}
