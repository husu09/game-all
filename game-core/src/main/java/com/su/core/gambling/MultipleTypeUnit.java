package com.su.core.gambling;

public class MultipleTypeUnit {

	/**
	 * 获取倍数
	 */
	public static MultipleType getMultiple(CardType cardType, Card[] cards) {
		switch (cardType) {
		case ZHA_DAN:
			if (cards.length == 6)
				return MultipleType.LIU_ZHA;
			if (cards.length == 7)
				return MultipleType.QI_ZHA;
			if (cards.length == 8)
				return MultipleType.TIAN_ZHA;
			break;
		case T_510K:
			if (CardUnit.isTongHua(cards)) 
				return MultipleType.TONG_HUA_510K;
			else
				return MultipleType.F_510K;
		case WANG_ZHA:
			return MultipleType.WANG_ZHA;

		default:
			break;
		}
		return null;
	}
}
