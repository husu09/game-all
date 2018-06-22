package com.su.core.gambling.assist.card;

import org.springframework.stereotype.Component;

import com.su.core.gambling.Card;
import com.su.core.gambling.enums.CardType;

@Component
public class DuiZi extends BasicCardAssist {

	@Override
	public CardType getCardType() {
		return CardType.DUI_ZI;
	}

	@Override
	public boolean verify(Card[] cards) {
		if (cards.length != 2 || cards[0].getValue() != cards[1].getValue() || cards[0].getValue() > Card.CARD_2)
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
