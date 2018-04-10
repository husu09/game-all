package com.su.play;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Table {
	
	/**
	 * 纸牌
	 */
	private int[] cards = new int[108];

	{
		int point = 3;
		for (int i = 0; i < 54; i++) {
			cards[i] = point;
			if ((i + 1) % 4 == 0 || point > 15) {
				point++;
			}
		}
		point = 3;
		for (int i = 0; i < 54; i++) {
			cards[i + 54] = point;
			if ((i + 1) % 4 == 0 || point > 15) {
				point++;
			}
		}

	}

	/**
	 * 玩家
	 */
	private Player[] players = new Player[4];
	/**
	 * 记分
	 */
	/**
	 * 名次
	 */
	/**
	 * 倍数
	 */
	/**
	 * 加入
	 */
	/**
	 * 退出
	 */
	/**
	 * 洗牌
	 */
	public void shuffle() {
		
	}
	/**
	 * 发牌
	 */
	public void licensing(){
		
	}
	/**
	 * 结算
	 */
	public static void main(String[] args) {
		Table t = new Table();
		System.out.println(Arrays.toString(t.cards));
	}
}
