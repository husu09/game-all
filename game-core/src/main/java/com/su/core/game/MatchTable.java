package com.su.core.game;

import com.su.core.game.enums.PlayerState;
import com.su.core.game.enums.TableState;
import com.su.msg.MatchSiteMsg.Match_;
import com.su.msg.TableMsg.Quit_;

/**
 * 可匹配的牌桌
 */
public abstract class MatchTable extends Table {

	protected MatchSite matchSite;

	public MatchTable(Site site) {
		super(site);
		matchSite = (MatchSite) site;
	}
	
	@Override
	public void doExit(GamePlayer gamePlayer) {
		
		if (state == TableState.CLOSE) {
			// 解散牌桌，准备中的玩家重新加入队列
			for (GamePlayer otherPlayer : this.players) {
				if (gamePlayer.equals(otherPlayer))
					continue;
				if (otherPlayer.getState() == PlayerState.READY) {
					matchSite.addPlayerToMatch(otherPlayer.getPlayerContext(), true);
					otherPlayer.getPlayerContext().write(Match_.newBuilder());
				}
				otherPlayer.clean();
			}
			// 牌桌
			clean();
			this.matchSite.getIdleTableQueue().offer(this);
		} else {
			gamePlayer.setAuto(1);
			gamePlayer.getPlayerContext().setGamePlayer(null);
			gamePlayer.getPlayerContext().write(Quit_.newBuilder());
		}
	}

	@Override
	public void doWaitClose() {
		// 结算超时
		for (GamePlayer otherGamePlayer : this.players) {
			if (otherGamePlayer.getState() != PlayerState.READY) {
				otherGamePlayer.clean();
				otherGamePlayer.getPlayerContext().write(Quit_.newBuilder());
			} else {
				// 准备中的玩家重新加入匹配队列
				otherGamePlayer.clean();
				matchSite.addPlayerToMatch(otherGamePlayer.getPlayerContext(), true);
				otherGamePlayer.getPlayerContext().write(Match_.newBuilder());
			}
		}
		// 牌桌
		clean();
		this.matchSite.getIdleTableQueue().offer(this);

	}

}
