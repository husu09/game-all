package com.su.core.gambling.service;

import com.su.core.gambling.Card;
import com.su.core.gambling.GamePlayer;
import com.su.core.gambling.enums.CallType;
import com.su.core.gambling.enums.PlayerState;
import com.su.core.gambling.enums.Team;

public class TeamService {
	/**
	 * 出牌时处理玩家队伍
	 * */
	public void doPlayerTeamWithDraw() {
		// 出牌时处理玩家队伍
		if (this.callType != CallType.LIGHT ) {
		for (Card card : cards) {
			if (card.equals(this.callCard)) {
				if (player.getIndex() != this.callOp) {
					// 叫牌
					GamePlayer callPlayer = this.players[this.callOp];
					player.setTeam(callPlayer.getTeam());
					for (GamePlayer otherPlayer : this.players) {
						if (otherPlayer.equals(player) || otherPlayer.equals(callPlayer))
							continue;
						otherPlayer.setTeam(Team.BLUE);
					}
				} else if (card != this.callCard && card.equals(this.callCard)) {
					// 暗叫
					eddToMultiple(CallType.CALL, null);
					addToMultiple(CallType.DARK, null);
					player.setScore(0);
					for (GamePlayer otherPlayer : this.players) {
						if (otherPlayer.equals(player))
							continue;
						otherPlayer.setTeam(Team.BLUE);
						// 清空积分
						otherPlayer.setScore(0);
					}
				}

			}
		}
		}
	}
	
	/**
	 * 出完牌后处理玩家队伍
	 * */
	public void doPlayerTeamWithFinish() {}
	/**
	 * 结束后处理玩家队伍
	 * */
	public void doPlayerTeamWithClose() {}
	
	/**
	 * 获取任意一方的人数
	 */
	private int getTeamCount(Team team) {
		int count = 0;
		for (GamePlayer otherPlayer : this.players) {
			if (team == otherPlayer.getTeam())
				count++;
		}
		return count;
	}

	/**
	 * 获取最后名的玩家
	 */
	private GamePlayer getLastPlayer() {
		GamePlayer lastPlayer = null;
		for (GamePlayer otherPlayer : this.players) {
			if (otherPlayer.getState() != PlayerState.FINISH) {
				lastPlayer = otherPlayer;
				break;
			}
		}
		return lastPlayer;
	}

	/**
	 * 添加排行
	 */
	private int addToRanks(GamePlayer player) {
		int rank = -1;
		for (int i = 0; i < this.ranks.length; i++) {
			if (this.ranks[i] == null) {
				this.ranks[i] = player.getIndex();
				rank = i;
				break;
			}
		}
		if (rank == -1) {
			logger.error("{} Rank is error {}", player.getId(), rank);
		}
		return rank;
	}

	/**
	 * 排行数量
	 */
	private int getRanksCount() {
		int count = 0;
		for (int i = 0; i < this.ranks.length; i++) {
			if (this.ranks[i] != null)
				count++;
			else
				break;
		}
		return count;
	}

}
