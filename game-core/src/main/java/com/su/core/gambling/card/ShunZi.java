package com.su.core.gambling.card;

import org.springframework.stereotype.Component;

import com.su.core.gambling.Card;
import com.su.core.gambling.enums.CardType;

@Component
public class ShunZi extends BaseCardProcessor {

	@Override
	public CardType getCardType() {
		return CardType.SHUN_ZI;
	}

	@Override
	public boolean verify(Card[] cards) {
		if (cards.length < 3)
			return false;
		Card card = cards[0];
		if (card.getValue() > Card.CARD_A)
			return false;
		for (int i = 1; i < cards.length; i++) {
			if (cards[i].getValue() > Card.CARD_A)
				return false;
			if (cards[i].getValue() != card.getValue() + 1) {
				return false;
			}
			card = cards[i];
		}
		return true;
	}

	@Override
	public boolean compare(Card[] cards, CardType lastCardType, Card[] lastCards) {
		if (getCardType() == lastCardType) {
			if (cards.length != lastCards.length)
				return false;
			if (Card.getMax(cards).getValue() > Card.getMax(lastCards).getValue())
				return true;
		}
		return false;
	}

}
