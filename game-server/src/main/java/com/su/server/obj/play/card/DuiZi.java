package com.su.server.obj.play.card;

import org.springframework.stereotype.Component;

import com.su.server.obj.play.Card;
import com.su.server.obj.play.CardType;

@Component
public class DuiZi extends BaseCardProcessor  {

	
	@Override
	public CardType getCardType() {
		return CardType.DUI_ZI;
	}

	@Override
	public boolean verify(Card[] cards) {
		if (cards.length != 2)
			return false;
		return false;
	}

	@Override
	public boolean compare(Card[] cards, CardType lastCardType, Card[] lastCards) {
		return false;
	}

}
