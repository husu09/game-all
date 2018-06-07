package com.su.core.gambling;

import com.su.core.gambling.Card;
import com.su.core.gambling.enums.CardType;

/**
 * 自动出牌
 * */
public class AutoCards {
	private CardType cardType;
	private Card[] cards;
	
	public CardType getCardType() {
		return cardType;
	}
	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}
	public Card[] getCards() {
		return cards;
	}
	public void setCards(Card[] cards) {
		this.cards = cards;
	}
	
}
