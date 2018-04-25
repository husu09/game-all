package com.su.server.obj.play.card;

import com.su.server.obj.play.Card;
import com.su.server.obj.play.CardType;

public interface CardProcessor {
	public boolean verify(Card[] cards);
	public boolean compare(Card[] cards, CardType lastCardType, Card[] lastCards);
}
