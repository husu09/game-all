package com.su.core.gambling.assist;

import com.su.core.gambling.AutoCards;
import com.su.core.gambling.Card;
import com.su.core.gambling.GamePlayer;
import com.su.core.gambling.enums.CardType;

public class AIAssist {
	/**
	 * 自动出牌
	 * @param lastPlayer 上游玩家
	 * @param lastCardType 上游玩家出牌类型
	 * @param lastCards	上游玩家出牌
	 * @param player	当前玩家
	 * @return
	 */
	public AutoCards getAutoResult(GamePlayer lastPlayer, CardType lastCardType, Card[] lastCards, GamePlayer player) {
		AutoCards autoResult = null;
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
	
	
}
