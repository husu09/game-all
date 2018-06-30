package com.su.core.game;

import java.util.ArrayDeque;
import java.util.Deque;

import com.su.common.constant.GameConst;
import com.su.config.SiteCo;
import com.su.core.context.PlayerContext;
import com.su.core.game.enums.PlayerState;

public class ClassicSite extends Site implements IMatch {
	/**
	 * 匹配中的玩家队列
	 */
	private Deque<GamePlayer> playerDeque = new ArrayDeque<>();

	private SiteCo siteCo;

	public ClassicSite(SiteCo siteCo) {
		this.siteCo = siteCo;
		// 初始化牌桌
		for (int i = 0; i < siteCo.getInitTableNum(); i++) {
			Table table = new ClassicTable(this);
			getIdleTableQueue().offer(table);
		}
	}
	
	@Override
	public synchronized void addPlayerToMatch(PlayerContext playerContext, boolean isFirst) {
		// 验证是否可以加入匹配队列
		if (playerContext.getGamePlayer() == null)
			new GamePlayer(playerContext);
		else if (playerDeque.contains(playerContext.getGamePlayer()))
			return;
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
		if (playerDeque.size() < GameConst.PLAYER_COUNT)
			return;
		GamePlayer[] gamePlayers = new GamePlayer[GameConst.PLAYER_COUNT];
		for (int i = 0; i < GameConst.PLAYER_COUNT; i++) {
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
		Table table = getIdleTableQueue().poll();
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
	public synchronized void removePlayerFromMatch(GamePlayer gamePlayer) {
		if (playerDeque.remove(gamePlayer))
			getPlayerNum().decrementAndGet();
	}

	/**
	 * 获取配置对象
	 */
	@Override
	public SiteCo getSiteCo() {
		return siteCo;
	}

}
