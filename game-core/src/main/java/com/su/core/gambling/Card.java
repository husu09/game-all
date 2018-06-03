package com.su.core.gambling;

import com.su.core.gambling.enums.Suit;

public class Card implements Comparable<Card> {
	/**
	 * 一幅牌的张数
	 */
	public static final int CARDS_NUM = 54;
	
	/**
	 * 玩家手牌张数
	 */
	public static final int HAND_CARDS_NUM = CARDS_NUM * 2 / 4;

	/**
	 * 卡牌对应的值
	 */
	public static final int CARD_3 = 3;
	public static final int CARD_4 = 4;
	public static final int CARD_5 = 5;
	public static final int CARD_6 = 6;
	public static final int CARD_7 = 7;
	public static final int CARD_8 = 8;
	public static final int CARD_9 = 9;
	public static final int CARD_10 = 10;
	public static final int CARD_J = 11;
	public static final int CARD_Q = 12;
	public static final int CARD_K = 13;
	public static final int CARD_A = 14;
	public static final int CARD_2 = 15;
	public static final int CARD_XIAO_WANG = 16;
	public static final int CARD_DA_WANG = 17;

	/**
	 * 一幅牌
	 */
	public static final Card[] ONE_CARDS;

	/**
	 * 初始化一幅牌
	 */
	static {
		Card[] cards = new Card[CARDS_NUM];
		int value = CARD_3;
		int suit = Suit.FANG_KUAI.ordinal();
		for (int i = 0; i < CARDS_NUM; i++) {
			if (value + i == CARD_XIAO_WANG) {
				cards[i] = new Card(CARD_XIAO_WANG, null);
				continue;
			}
			if (value + i == CARD_DA_WANG) {
				cards[i] = new Card(CARD_DA_WANG, null);
				continue;
			}
			cards[i] = new Card(value + i, Suit.values()[suit]);
			if ((i + 1) % 4 == 0) {
				value++;
				suit = Suit.FANG_KUAI.ordinal();
			} else {
				suit++;
			}
		}
		ONE_CARDS = cards;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (suit != other.suit)
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Card [value=" + value + ", suit=" + suit + "]";
	}

	public int getValue() {
		return value;
	}

	public Suit getSuit() {
		return suit;
	}

}
