package com.su.server.obj.play;

import com.su.proto.PlayProto.CardPro;

public class Card implements Comparable<Card> {
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

	@Override
	public int compareTo(Card o) {
		if (value > o.getValue())
			return 1;
		else if (value < o.getValue())
			return -1;
		if (suit.ordinal() > o.getSuit().ordinal())
			return 1;
		else if (suit.ordinal() < o.getSuit().ordinal())
			return -1;
		return 0;
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
