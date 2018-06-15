package com.su.core.gambling.util;

import com.su.core.gambling.Card;

public class CardUtil {
	/**
	 * 获取最大值
	 */
	public static Card getMax(Card[] cards) {
		Card max = cards[0];
		for (int i = 1; i < cards.length; i++) {
			if (cards[i].getValue() > max.getValue())
				max = cards[i];
		}
		return max;
	}

	/**
	 * 是否是同花
	 */
	public static boolean isTongHua(Card[] cards) {
		Card card = cards[0];
		for (int i = 1; i < cards.length; i++) {
			if (cards[i].getSuit() != card.getSuit())
				return false;
		}
		return true;

	}
	
}
