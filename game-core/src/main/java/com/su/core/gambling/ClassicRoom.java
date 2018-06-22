package com.su.core.gambling;

import java.util.concurrent.ConcurrentLinkedDeque;

import com.su.core.context.PlayerContext;
import com.su.core.gambling.enums.PlayerState;

public class ClassicRoom extends BasicRoom implements IMatch {
	/**
	 * 匹配中的玩家队列
	 */
	private ConcurrentLinkedDeque<GamePlayer> playerDeque = new ConcurrentLinkedDeque<>();
	
	@Override
	public void addPlayerToMatch(PlayerContext playerContext, boolean isFirst) {
		// 验证是否可以加入匹配队列
		if (playerDeque.contains(playerContext))
			return;
		if (playerContext.getGamePlayer() == null)
			new GamePlayer(playerContext);
		else if (playerContext.getGamePlayer().getState() != null)
			return;
		// 加入队列
		if (isFirst)
			playerDeque.offerFirst(playerContext.getGamePlayer());
		else
			playerDeque.offer(playerContext.getGamePlayer());
		getPlayerNum().incrementAndGet();
		// 尝试开始游戏
		// 尝试从队列中获取4个玩家
		GamePlayer[] gamePlayers = new GamePlayer[4];
		for (int i = 0; i < 4; i++) {
			gamePlayers[i] = playerDeque.poll();
			if (gamePlayers[i] == null) {
				// 不足4人时重新排队
				for (GamePlayer oGamePlayer : gamePlayers) {
					if (oGamePlayer == null)
						break;
					playerDeque.offerFirst(oGamePlayer);
				}
				return;
			}
		}
		// 人数足够时开始游戏
		BasicTable table = getIdleTableQueue().poll();
		if (table == null) {
			table = new ClassicTable(this);
		}
		// 设置玩家状态
		for (GamePlayer otherPlayers : gamePlayers)
			otherPlayers.setState(PlayerState.WAIT);
		table.getActor().setPlayers(gamePlayers);
		table.getActor().deal();
	}

	@Override
	public void removePlayerFromMatch(GamePlayer gamePlayer) {
		if (playerDeque.remove(gamePlayer))
			getPlayerNum().decrementAndGet();
	}



}
