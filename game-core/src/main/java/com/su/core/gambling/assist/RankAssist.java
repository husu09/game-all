package com.su.core.gambling.assist;

import com.su.core.gambling.Card;
import com.su.core.gambling.GamePlayer;
import com.su.core.gambling.enums.CallType;
import com.su.core.gambling.enums.MultipleType;
import com.su.core.gambling.enums.PlayerState;
import com.su.core.gambling.enums.Team;

public class RankAssist {
	/**
	 * 处理队伍、排名、结束
	 */
	private boolean doTeamRankClose(GamePlayer player) {
		if (isFinish(player)) {
			player.setState(PlayerState.FINISH);
			// 全部出完后还没有队伍，则可以判断是蓝方
			if (player.getTeam() == null)
				player.setTeam(Team.BLUE);
			// 排名
			addToRanks(player);
			// 判断牌局是否结束
			// 处理玩家队伍和排名
			Team winTeam = null;
			if (this.callType == CallType.LIGHT || this.callType == CallType.DARK) {
				winTeam = this.players[ranks[0]].getTeam();
				for (GamePlayer otherPlayer : this.players) {
					if (player.getTeam() == null)
						player.setTeam(Team.BLUE);
					otherPlayer.setState(PlayerState.FINISH);
					// 排名
					addToRanks(otherPlayer);
				}
			} else if (this.ranks[0] != null && this.ranks[1] != null
					&& this.players[ranks[0]].getTeam() == this.players[ranks[1]].getTeam()) {
				winTeam = this.players[ranks[0]].getTeam();
				for (GamePlayer otherPlayer : this.players) {
					if (player.getTeam() == null)
						player.setTeam(this.players[ranks[0]].getTeam().getReverse());
					otherPlayer.setState(PlayerState.FINISH);
					// 排名
					addToRanks(otherPlayer);
				}
				// 全胜
				addToMultiple(MultipleType.QUAN_SHENG, null);
				// 满分
				int winScore = 0;
				for (GamePlayer otherPlayer : this.players) {
					if (otherPlayer.getTeam() == winTeam)
						winScore += otherPlayer.getScore();
				}
				if (winScore >= 200)
					addToMultiple(MultipleType.MAN_FEN, null);
			} else if (getRanksCount() >= 3) {
				GamePlayer lastPlayer = getLastPlayer();
				if (player.getTeam() == null)
					player.setTeam(this.players[this.ranks[2]].getTeam().getReverse());
				lastPlayer.setState(PlayerState.FINISH);
				// 排名
				addToRanks(lastPlayer);

				// 最后出牌玩家分数处理
				// 手中的分数给第一名
				this.players[this.ranks[0]].addScore(lastPlayer.getScore());
				// 最后玩家得分作废
				lastPlayer.setScore(0);
				// 未出的分数直接加个对方
				int leftScore = Card.getScore(lastPlayer.getHandCards());

				// 最后剩余轮分处理
				if (this.roundScore != 0) {
					player.setScore(player.getScore() + this.roundScore);
					this.roundScore = 0;
				}
				// 计算总分
				int redScore = 0;
				int blueScore = 0;
				for (GamePlayer otherPlayer : this.players) {
					if (otherPlayer.getTeam() == Team.RED)
						redScore += otherPlayer.getScore();
					else
						blueScore += otherPlayer.getScore();
				}
				if (lastPlayer.getTeam() == Team.RED)
					blueScore += leftScore;
				else
					redScore += blueScore;
				if (redScore > blueScore) {
					winTeam = Team.RED;
				} else if (redScore == blueScore) {
					winTeam = this.players[ranks[0]].getTeam();
				} else {
					winTeam = Team.BLUE;
				}
				// 满分
				if (redScore >= 200 || blueScore >= 200)
					addToMultiple(MultipleType.MAN_FEN, null);

			}
			if (winTeam != null) {
				close(winTeam);
				return true;
			}
		}
		return false;
	}


	
}
