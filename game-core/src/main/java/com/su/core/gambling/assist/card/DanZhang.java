package com.su.core.gambling.assist.card;

import org.springframework.stereotype.Component;

import com.su.core.gambling.Card;
import com.su.core.gambling.enums.CardType;

@Component
public class DanZhang extends BasicCardAssist {

	@Override
	public CardType getCardType() {
		return CardType.DAN_ZHANG;
	}

	@Override
	public boolean verify(Card[] cards) {
		if (cards.length != 1)
			return false;
		return true;
	}

	@Override
	public boolean compare(Card[] cards, CardType lastCardType, Card[] lastCards) {
		if (lastCardType == getCardType()) {
			if (cards[0].getValue() > lastCards[0].getValue()) {
				return true;
			}
		}
		return false;
	}

}
