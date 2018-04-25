package com.su.server.obj.play.card;

import org.springframework.stereotype.Component;

import com.su.server.obj.play.Card;
import com.su.server.obj.play.CardType;
import com.su.server.obj.play.CardUnit;

@Component
public class T510K  extends BaseCardProcessor {



	@Override
	public CardType getCardType() {
		return CardType.T_510K;
	}

	@Override
	public boolean verify(Card[] cards) {
		if (cards.length != 3)
			return false;
		if (cards[0].getValue() != Card.CARD_K)
			return false;
		if (cards[1].getValue() != Card.CARD_10)
			return false;
		if (cards[2].getValue() != Card.CARD_5)
			return false;
		return true;
	}

	@Override
	public boolean compare(Card[] cards, CardType lastCardType, Card[] lastCards) {
		if (getCardType() == lastCardType) {
			boolean cardsIsTH = CardUnit.isTongHua(cards);
			boolean lastCardsIsTH = CardUnit.isTongHua(lastCards);
			if (cardsIsTH && lastCardsIsTH) {
				if (cards[0].getSuit().ordinal() <= lastCards[0].getSuit().ordinal())
					return false;
			}
			if (!cardsIsTH)
				return false;
		} else if (lastCardType.equals(CardType.ZHA_DAN)) {
			if (CardUnit.isTongHua(cards)) {
				if (lastCards.length >= 7)
					return false;
			} else {
				if (lastCards.length >= 6)
					return false;
			}
		} else if (lastCardType.equals(CardType.WANG_ZHA)) {
			return false;
		}
		return true;
	}

}
