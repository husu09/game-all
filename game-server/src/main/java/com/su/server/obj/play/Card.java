package com.su.server.obj.play;

import com.su.proto.PlayProto.PCard;

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

	/**
	 * 一幅牌的张数
	 */
	public static final int CARDS_NUM = 54;

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
	 * 获取一幅牌
	 */
	public static Card[] getADeckOfCards() {
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
		return cards;
	}

	public PCard toProto() {
		PCard.Builder builder = PCard.newBuilder();
		return toProto(builder);
	}

	public PCard toProto(PCard.Builder builder) {
		builder.setValue(value);
		if (suit != null)
			builder.setSuit(suit.ordinal());
		PCard pCard = builder.build();
		builder.clear();
		return pCard;
	}

}
