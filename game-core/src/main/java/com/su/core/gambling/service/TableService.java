package com.su.core.gambling.service;

import com.su.core.gambling.Card;
import com.su.core.gambling.GamePlayer;
import com.su.core.gambling.enums.PlayerState;

public class TableService {
	/**
	 * 是否是同一状态
	 */
	private boolean isSameState(PlayerState playerState) {
		for (GamePlayer otherPlayer : this.players) {
			if (otherPlayer.getState() != playerState)
				return false;
		}
		return true;
	}
	
	/**
	 * 是否完成
	 */
	private boolean isFinish(GamePlayer player) {
		for (Card card : player.getHandCards()) {
			if (card != null)
				return false;
		}
		return true;
	}
}
