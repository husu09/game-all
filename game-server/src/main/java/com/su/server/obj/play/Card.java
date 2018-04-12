package com.su.server.obj.play;

public class Card {
	/**
	 * 牌面
	 * */
	private int value;
	/**
	 * 花色
	 * */
	private Suit suit;

	public Card(int value, Suit suit) {
		this.value = value;
		this.suit = suit;
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
