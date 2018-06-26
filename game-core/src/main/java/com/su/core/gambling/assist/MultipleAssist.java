package com.su.core.gambling.assist;

import org.springframework.stereotype.Component;

import com.su.core.gambling.BasicTable;
import com.su.core.gambling.Card;
import com.su.core.gambling.ITable;
import com.su.core.gambling.enums.MultipleType;

@Component
public class MultipleAssist {
	/**
	 * 增加公共倍数
	 */
	public void addMultiple(BasicTable table, Object o, Card[] cards) {
		MultipleType multiple = MultipleType.getMultiple(o, cards);
		if (multiple != null) {
			table.getMultiples()[multiple.ordinal()] += multiple.getValue();
		}
	}

	/**
	 * 扣除公共倍数
	 */
	public void eddMultiple(BasicTable table, Object o, Card[] cards) {
		MultipleType multiple = MultipleType.getMultiple(o, cards);
		if (multiple != null && table.getMultiples()[multiple.ordinal()] >= multiple.getValue()) {
			table.getMultiples()[multiple.ordinal()] -= multiple.getValue();
		}
	}
}
