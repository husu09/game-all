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
	private ConcurrentLinkedQueue<Table> idleTableQueue = new ConcurrentLinkedQueue<>();
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
			idleTableQueue.offer(table);
		}
	}

	/**
	 * 添加玩家到匹配队列
	 */
	public void addPlayerToMatch(PlayerContext playerContext, boolean isFirst) {
		// 在匹配队列中
		if (playerDeque.contains(playerContext))
			return;
		if (playerContext.getGamePlayer() == null)
			playerContext.setGamePlayer(new GamePlayer(playerContext));
		if (isFirst)
			playerDeque.offerFirst(playerContext.getGamePlayer());
		else
			playerDeque.offer(playerContext.getGamePlayer());
		playerNum.incrementAndGet();
		tryStart();
	}

	/**
	 * 从匹配队列删除玩家
	 */
	public void removePlayerFromMatch(GamePlayer gamePlayer) {
		if (playerDeque.remove(gamePlayer))
			playerNum.decrementAndGet();
	}

	/**
	 * 尝试开始游戏
	 */
	public void tryStart() {
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
		Table table = idleTableQueue.poll();
		if (table == null) {
			table = new Table(this);
		}
		table.getActor().setPlayers(gamePlayers);
		table.getActor().deal();
	}

	/**
	 * 检测牌桌超时
	 */
	public void doWaitTable() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				Table table = waitTableQueue.take();
				table.getActor().doWaitTable();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检测玩家超时
	 */
	public void doWaitGamePlayer() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				GamePlayer gamePlayer = waitGamePlayerQueue.take();
				gamePlayer.getTable().getActor().doWaitGamePlayer(gamePlayer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ConcurrentLinkedQueue<Table> getIdleTableQueue() {
		return idleTableQueue;
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
