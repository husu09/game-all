package com.su.server.obj.play;

import com.su.proto.PlayProto.CardPro;

public class Card {
	/**
	 * 牌面
	 */
	private int value;
	/**
	 * 花色
	 */
	private Suit suit;

	public Card(int value, Suit suit) {
		this.value = value;
		this.suit = suit;
	}

	public CardPro toProto() {
		CardPro.Builder builder = CardPro.newBuilder();
		builder.setValue(value);
		builder.setSuit(suit.ordinal());
		return builder.build();
	}

	public CardPro toProto(CardPro.Builder builder) {
		if (builder == null)
			builder = CardPro.newBuilder();
		builder.setValue(value);
		builder.setSuit(suit.ordinal());
		return builder.build();
	}

	public int getValue() {
		return value;
	}

	public Suit getSuit() {
		return suit;
	}

	@Override
	public String toString() {
		return "Card [value=" + value + ", suit=" + suit + "]";
	}

}
