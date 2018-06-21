package com.su.core.gambling.util;

import com.su.core.gambling.Card;
import com.su.core.gambling.GamePlayer;
import com.su.core.gambling.enums.CardType;

public class AIService {
	/**
	 * 自动出牌
	 * @param lastPlayer 上游玩家
	 * @param lastCardType 上游玩家出牌类型
	 * @param lastCards	上游玩家出牌
	 * @param player	当前玩家
	 * @return
	 */
	public AutoResult getAutoResult(GamePlayer lastPlayer, CardType lastCardType, Card[] lastCards, GamePlayer player) {
		AutoResult autoResult = null;
		if (lastPlayer == null || lastPlayer.equals(player)) {
			// 获取最小的牌
		} else if (player.getTeam() == lastPlayer.getTeam()) {
			// 己方出牌时跳过
			return null;
		} else {
			// 获取比对方大的牌
		}
		
		return autoResult;
	}
	
	/**
	 * 获取单张
	 * */
	private void getDanZhang() {
		
	}
	/**
	 * 获取对子
	 * */
	private void getDuiZi() {
		
	}
	/**
	 * 获取连对
	 * */
	private void getLianDui() {
		
	}
	
	/**
	 * 获取顺子
	 * */
	private void getShunZi() {
		
	}
	
	/**
	 * 获取510K
	 * */
	private void get510K() {
		
	}
	
	/**
	 * 获取炸弹
	 * */
	private void getZhaDan() {
		
	}
	
	/**
	 * 获取同花510K
	 * */
	private void getDanZhangT510K() {
		
	}
	
	/**
	 * 获取王炸
	 * */
	private void getWangZha() {
		
	}
}
