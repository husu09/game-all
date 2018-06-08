package com.su.core.gambling;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.su.config.SiteCo;
import com.su.core.context.PlayerContext;

/**
 * 场所
 */
public class Site {
	/**
	 * 玩家人数
	 */
	private AtomicInteger playerNum = new AtomicInteger();
	/**
	 * 匹配中的玩家队列
	 */
	private ConcurrentLinkedDeque<GamePlayer> playerDeque = new ConcurrentLinkedDeque<>();
	/**
	 * 空闲牌桌队列
	 */
	private ConcurrentLinkedQueue<Table> tableQueue = new ConcurrentLinkedQueue<>();
	/**
	 * 需要操作的牌桌队列
	 */
	private DelayQueue<Table> waitTableQueue = new DelayQueue<>();
	/**
	 * 需要操作的玩家队列
	 */
	private DelayQueue<GamePlayer> waitGamePlayerQueue = new DelayQueue<>();
	
	private SiteCo siteCo;

	public Site(SiteCo siteCo) {
		this.siteCo = siteCo;
		// 初始化牌桌
		for (int i = 0; i < siteCo.getInitTableNum(); i++) {
			Table table = new Table(this);
			tableQueue.offer(table);
		}
	}

	/**
	 * 开始匹配
	 */
	public void startMatch(PlayerContext playerContext) {
		if (playerContext.getGamePlayer() == null) {
			playerContext.setGamePlayer(new GamePlayer(playerContext));
		} else if (playerContext.getGamePlayer().getState() != null) {
			// 已经在游戏中
			return;
		}
		playerNum.incrementAndGet();
		playerDeque.offerLast(playerContext.getGamePlayer());
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
		Table table = tableQueue.poll();
		if (table == null) {
			table = new Table(this);
		}
		table.getActor().setPlayers(gamePlayers);
	}
	
	public ConcurrentLinkedDeque<GamePlayer> getPlayerDeque() {
		return playerDeque;
	}

	public ConcurrentLinkedQueue<Table> getTableQueue() {
		return tableQueue;
	}

	public DelayQueue<Table> getWaitTableQueue() {
		return waitTableQueue;
	}

	public DelayQueue<GamePlayer> getWaitGamePlayerQueue() {
		return waitGamePlayerQueue;
	}

	public SiteCo getSiteCo() {
		return siteCo;
	}
	
}
