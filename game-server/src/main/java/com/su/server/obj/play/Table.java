package com.su.server.obj.play;

import java.util.Arrays;

import com.su.common.util.CommonUtils;

/**
 * 牌桌对象
 */
public class Table {
	/**
	 * 玩家
	 */
	private GamePlayer[] players;
	/**
	 * 牌
	 */
	private Card[] cards = new Card[108];
	/**
	 * 状态
	 */
	private TableState state = TableState.IDLE;
	/**
	 * 庄家
	 */
	private int dealerIndex = 0;
	/**
	 * 牌权玩家
	 * */
	private int holdIndex = 0;
	

	public Table() {
		// 生成牌
		for (int j = 0; j < 2; j++) {
			int index = 0;
			int value = 3;
			int suit = 0;
			for (int i = 1; i <= 52; i++) {
				cards[index] = new Card(value, Suit.values()[suit]);
				System.out.println(cards[index]);
				if (i % 4 == 0) {
					value++;
					suit = 0;
				} else {
					suit++;
				}
				index++;
			}
			cards[index] = new Card(value, Suit.values()[Suit.values().length - 1]);
			index++;
			value++;
			cards[index] = new Card(value, Suit.values()[Suit.values().length - 1]);
		}

	}

	/**
	 * 开始游戏
	 */
	public void start(GamePlayer[] players) {
		this.players = players;
		this.state = TableState.START;
		shuffle();
		deal();
	}
	
	

	/**
	 * 洗牌
	 */
	private void shuffle() {
		for (int i = 0; i < cards.length; i++) {
			Card tmp = cards[1];
			int r = CommonUtils.range(i, cards.length);
			cards[r] = tmp;
		}
	}

	/**
	 * 发牌
	 */
	private void deal() {
		int cycle = 0;
		for (int i = 0; i < cards.length; i++) {
			for (int j = dealerIndex; j < players.length; j++) {
				players[j].getHandCards()[cycle] = cards[i];
				i++;
			}
			cycle++;
		}
	}

	/**
	 * 出牌
	 */

	/**
	 * 过牌
	 */
	
	/**
	 * 叫牌
	 * */
	public void call() {
		
	}

	public static void main(String[] args) {
		new Table();
	}
}
