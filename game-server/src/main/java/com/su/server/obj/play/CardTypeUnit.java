package com.su.server.obj.play;

public class CardTypeUnit {

	/**
	 * 牌型验证
	 */
	public static boolean verify(CardType cardType, Card[] cards) {
		switch (cardType) {
		case DAN_ZHANG:
			if (cards.length != 1)
				return false;
			break;
		case DUI_ZI:
			if (cards.length != 2)
				return false;
			if (cards[0].getValue() != cards[1].getValue())
				return false;
			if (cards[0].getValue() > 15)
				return false;
			break;
		case LIAN_DUI: {
			if (cards.length < 6)
				return false;
			if (cards.length % 2 != 0)
				return false;
			Card card = cards[0];
			if (card.getValue() > 14)
				return false;
			for (int i = 1; i < cards.length; i++) {
				if (cards[i].getValue() > 14)
					return false;
				if (cards[i].getValue() != card.getValue() && cards[i].getValue() != card.getValue() - 1) {
					return false;
				}
				card = cards[i];

			}
		}
			break;
		case SHUN_ZI: {
			if (cards.length < 3)
				return false;
			Card card = cards[0];
			if (card.getValue() > 14)
				return false;
			for (int i = 1; i < cards.length; i++) {
				if (cards[i].getValue() > 14)
					return false;
				if (cards[i].getValue() != card.getValue() - 1) {
					return false;
				}
				card = cards[i];
			}

			break;
		}
		case ZHA_DAN: {
			if (cards.length < 3 || cards.length > 8)
				return false;
			Card card = cards[0];
			if (card.getValue() > 15)
				return false;
			for (int i = 1; i < cards.length; i++) {
				if (cards[i].getValue() > 15)
					return false;
				if (cards[i].getValue() != card.getValue()) {
					return false;
				}
			}
			break;
		}
		case T_510K:
			if (cards.length != 3)
				return false;
			if (cards[0].getValue() != 13)
				return false;
			if (cards[1].getValue() != 10)
				return false;
			if (cards[2].getValue() != 5)
				return false;

			break;
		case WANG_ZHA:
			if (cards.length != 2)
				return false;
			if (cards[0].getValue() < 16)
				return false;
			if (cards[1].getValue() < 16)
				return false;
			break;

		default:
			break;
		}
		return true;
	}

	/**
	 * 比较大小
	 */
	public static boolean compare(CardType cardType, Card[] cards, CardType lastCardType, Card[] lastCards) {
		switch (cardType) {
		case DAN_ZHANG:
			if (!cardType.equals(lastCards))
				return false;
			if (cards[0].getValue() <= lastCards[0].getValue())
				return false;
			break;
		case DUI_ZI:
			if (!cardType.equals(lastCards))
				return false;
			if (cards[0].getValue() <= lastCards[0].getValue())
				return false;
			break;
		case LIAN_DUI:
			if (!cardType.equals(lastCards))
				return false;
			if (cards.length != lastCards.length)
				return false;
			if (CardUnit.getMax(cards).getValue() <= CardUnit.getMax(lastCards).getValue())
				return false;
			break;
		case SHUN_ZI:
			if (!cardType.equals(lastCards))
				return false;
			if (cards.length != lastCards.length)
				return false;
			if (CardUnit.getMax(cards).getValue() <= CardUnit.getMax(lastCards).getValue())
				return false;
			break;
		case ZHA_DAN:
			if (cardType.equals(lastCards)) {
				if (cards.length < lastCards.length)
					return false;
				if (cards.length == lastCards.length) {
					if (cards[0].getValue() <= lastCards[0].getValue())
						return false;
				}
			} else if (lastCardType.equals(CardType.T_510K)) {
				if (CardUnit.isTongHua(lastCards)) {
					if (cards.length < 7)
						return false;
				} else {
					if (cards.length < 6)
						return false;
				}

			} else if (lastCardType.equals(CardType.WANG_ZHA)) {
				if (cards.length < 8)
					return false;
			}
			break;
		case T_510K:
			if (cardType.equals(lastCards)) {
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
			break;
		case WANG_ZHA:
			if (cardType.equals(lastCards)) {
				if (cards[0].getValue() + cards[1].getValue() <= lastCards[0].getValue() + lastCards[1].getValue())
					return false;
			} else if (lastCardType.equals(CardType.ZHA_DAN)) {
				if (lastCards.length >= 8)
					return false;
			} else if (lastCardType.equals(CardType.T_510K)) {

			}
			break;

		default:
			break;
		}
		return true;
	}

}
