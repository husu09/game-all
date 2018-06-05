	package com.su.core.gambling.card;

import com.su.core.gambling.Card;

public interface CardProcessor {
	public boolean verify(Card[] cards);
	public boolean compare(Card[] cards, CardType lastCardType, Card[] lastCards);
}
