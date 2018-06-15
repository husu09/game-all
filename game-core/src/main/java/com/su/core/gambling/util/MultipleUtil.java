package com.su.core.gambling.util;

import com.su.core.gambling.Card;
import com.su.core.gambling.enums.CallType;
import com.su.core.gambling.enums.CardType;
import com.su.core.gambling.enums.MultipleType;

public class MultipleUtil {

	/**
	 * 获取倍数
	 */
	public static MultipleType getMultiple(Object o, Card[] cards) {
		if (o instanceof CallType) {
			// 叫牌倍数
			if (o == CallType.CALL)
				return MultipleType.JIAO_PAI;
			else if (o == CallType.DARK)
				return MultipleType.AN_JIAO;
			else if (o == CallType.LIGHT)
				return MultipleType.MING_JIAO;
		} else if (o instanceof CardType) {
			// 出牌倍数
			if (o == CardType.T_510K) {
				if (CardUtil.isTongHua(cards))
					return MultipleType.TONG_HUA_510K;
				else
					return MultipleType.F_510K;
			} else if (o == CardType.ZHA_DAN) {
				if (cards.length == 6)
					return MultipleType.LIU_ZHA;
				if (cards.length == 7)
					return MultipleType.QI_ZHA;
				if (cards.length == 8)
					return MultipleType.TIAN_ZHA;

			} else if (o == CardType.WANG_ZHA) {
				return MultipleType.WANG_ZHA;
			}
		}
		return null;
	}
}
