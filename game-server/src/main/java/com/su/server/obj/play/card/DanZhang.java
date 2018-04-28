package com.su.server.obj.play.card;

import org.springframework.stereotype.Component;

import com.su.server.obj.play.Card;
import com.su.server.obj.play.CardType;

@Component
public class DanZhang extends BaseCardProcessor {

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