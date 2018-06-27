package com.su.core.gambling;

import com.su.core.gambling.enums.PlayerState;
import com.su.core.gambling.enums.TableState;
import com.su.msg.GamblingMsg.Match_;
import com.su.msg.GamblingMsg.Quit_;

/**
 * 可匹配的牌桌
 */
public abstract class MatchTable extends Table {

	protected MatchRoom matchRoom;

	public MatchTable(MatchRoom room) {
		super(room);
		matchRoom = room;
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
					matchRoom.addPlayerToMatch(otherPlayer.getPlayerContext(), true);
					otherPlayer.getPlayerContext().write(Match_.newBuilder());
				}
				otherPlayer.clean();
			}
			// 牌桌
			clean();
			this.room.getIdleTableQueue().offer(this);
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
				matchRoom.addPlayerToMatch(otherGamePlayer.getPlayerContext(), true);
				otherGamePlayer.getPlayerContext().write(Match_.newBuilder());
			}
		}
		// 牌桌
		clean();
		this.room.getIdleTableQueue().offer(this);

	}

}
