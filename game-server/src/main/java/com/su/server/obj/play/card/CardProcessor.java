package com.su.common.obj.play.card;

import com.su.common.obj.play.Card;
import com.su.common.obj.play.CardType;

public interface CardProcessor {
	public boolean verify(Card[] cards);
	public boolean compare(Card[] cards, CardType lastCardType, Card[] lastCards);
}
