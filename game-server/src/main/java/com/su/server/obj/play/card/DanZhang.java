package com.su.server.obj.play.card;

import com.su.server.obj.play.Card;
import com.su.server.obj.play.CardType;

public class DanZhang extends BaseCardProcessor {

	@Override
	public CardType getCardType() {
		return CardType.DAN_ZHANG;
	}

	@Override
	public boolean verify(Card[] cards) {
		if (cards.length == 1)
			return true;
		return false;
	}

	@Override
	public boolean compare(Card[] cards, CardType lastCardType, Card[] lastCards) {
		if (lastCardType == CardType.ZHA_DAN || lastCardType == CardType.T_510K || lastCardType == CardType.WANG_ZHA)
			return true;
		if (lastCardType == getCardType()) {
			if (lastCards[0].getValue() > cards[0].getValue()) {
				return true;
			}
		}
		return false;
	}

}
