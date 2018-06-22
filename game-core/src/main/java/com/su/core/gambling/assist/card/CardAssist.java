	package com.su.core.gambling.assist.card;

import com.su.core.gambling.Card;
import com.su.core.gambling.enums.CardType;

/**
 * 牌型辅助类
 * */
public interface CardAssist {
	public boolean verify(Card[] cards);
	public boolean compare(Card[] cards, CardType lastCardType, Card[] lastCards);
}
