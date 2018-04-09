package com.su.play.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.su.common.util.CommonUtils;

public class Table {
	private Card[] cards = new Card[108];
	private Player[] players = new Player[4];
	private int winnerIndex;
	
	private int MAX_NUM_OF_CARD = 54;
	/**
	 * 当前牌面
	 * */
	/**
	 * 被叫的牌
	 * */
	/**
	 * 玩家分组
	 * */
	/**
	 * 庄家
	 * */
	/**
	 * 分数
	 * */
	/**
	 * 番数
	 * */
	/**
	 * 名次
	 * */
	{
		int point = 3;
		for (int i = 0; i < MAX_NUM_OF_CARD; i++) {
			cards[i] = point;
			if ((i + 1) % 4 == 0 || point > 15) {
				point++;
			}
		}
		point = 3;
		for (int i = 0; i < MAX_NUM_OF_CARD; i++) {
			cards[MAX_NUM_OF_CARD + i] = point;
			if ((i + 1) % 4 == 0 || point > 15) {
				point++;
			}
		}
		System.out.println(Arrays.toString(cards));
	}

	/**
	 * 洗牌
	 */
	public void Shuffle() {
		for (int i = 0; i < cards.length; i++) {
			int tmp = cards[1];
			int r = CommonUtils.range(i, cards.length);
			cards[i] = cards[r];
			cards[r] = tmp;
		}
		System.out.println(Arrays.toString(cards));
	}

	/**
	 * 发牌
	 */
	public void deal() {
		int startIndex = winnerIndex;
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < players.length; i++) {
			map.put(i, 0);
		}
		for (int i = 0; i < cards.length; i++) {

			players[startIndex].getHandCards()[map.get(startIndex)] = cards[i];
			map.put(startIndex, map.get(startIndex) + 1);
			if ((i + 1) % players.length == 0) {
				startIndex = winnerIndex;
			}
			startIndex++;
		}

	}
	
	/**
	 * 坐下
	 * */
	/**
	 * 离开
	 * */
	/**
	 * 出牌
	 * */
	/**
	 * 过牌
	 * */
	/**
	 * 叫牌
	 * */
	/**
	 * 吼牌
	 * */
	/**
	 * 得分
	 * */
	/**
	 * 接风
	 * */
	
	
	

	public static void main(String[] args) {
		Table table = new Table();
		table.Shuffle();
	}
}
