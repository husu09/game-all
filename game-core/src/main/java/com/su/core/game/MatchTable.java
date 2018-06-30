package com.su.core.game;

import com.su.core.game.enums.PlayerState;
import com.su.core.game.enums.TableState;
import com.su.core.game.enums.Team;
import com.su.msg.GamblingMsg.Match_;
import com.su.msg.GamblingMsg.Quit_;
import com.su.msg.GamblingMsg.TableResult_.Builder;

/**
 * 可匹配的牌桌
 */
public abstract class MatchTable extends Table {
	
	protected IMatch match;
	
	public MatchTable(Site room) {
		super(room);
	}

	@Override
	public void doExit(GamePlayer gamePlayer) {
		// 自已退出清空数据
		gamePlayer.clean();
		if (state == TableState.CLOSE) {
			// 解散牌桌，准备中的玩家重新加入队列
			for (GamePlayer otherPlayer : this.players) {
				if (gamePlayer.equals(otherPlayer))
					continue;
				if (otherPlayer.getState() == PlayerState.READY) {
					match.addPlayerToMatch(otherPlayer.getPlayerContext(), true);
					otherPlayer.getPlayerContext().write(Match_.newBuilder());
				}
				otherPlayer.clean();
			}
			// 牌桌
			clean();
			this.site.getIdleTableQueue().offer(this);
		} else {
			gamePlayer.setAuto(1);
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
				match.addPlayerToMatch(otherGamePlayer.getPlayerContext(), true);
				otherGamePlayer.getPlayerContext().write(Match_.newBuilder());
			}
		}
		// 牌桌
		clean();
		this.site.getIdleTableQueue().offer(this);

	}

	@Override
	public void doClose(Builder builder, Team winTeam, int redMultiple, int blueMultiple) {
		builder.setBaseScore(this.match.getSiteCo().getBaseScore());
		for (GamePlayer otherPlayer : this.players) {
			TableResult tableResult = new TableResult();
			tableResult.setPlayerContext(otherPlayer.getPlayerContext());
			tableResult.setSiteCo(this.match.getSiteCo());
			if (otherPlayer.getTeam() == winTeam)
				tableResult.setWin(true);
			else
				tableResult.setWin(false);

			if (otherPlayer.getTeam() == Team.RED)
				tableResult.setMultiple(redMultiple);
			else
				tableResult.setMultiple(blueMultiple);
			builder.addPlayerResult(otherPlayer.getPlayerContext().getActor().doTableResult(tableResult));
		}
	}

}
