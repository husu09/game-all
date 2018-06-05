package com.su.core.gambling;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicInteger;

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
	 * 玩家游戏对象
	 * */
	private Map<Long, GamePlayer> gamePlayer = new ConcurrentHashMap<>();
	/**
	 * 玩家队列
	 */
	private ConcurrentLinkedDeque<Long> playerDeque = new ConcurrentLinkedDeque<>();
	/**
	 * 空闲牌桌队列
	 */
	private ConcurrentLinkedQueue<Table> tableQueue = new ConcurrentLinkedQueue<>();
	/**
	 * 等待中的牌桌队列
	 */
	private DelayQueue<Table> waitingTableQueue = new DelayQueue<>();
	/**
	 * 操作时间队列
	 */
	private DelayQueue<Table> deadLineQueue = new DelayQueue<>();

	
	/**
	 * 处理玩家加入
	 */
	public void startMatch(PlayerContext playerContext) {
		playerNum.incrementAndGet();
		playerDeque.offerLast(new GamePlayer(playerContext));
		// 尝试从队列中获取4个玩家
		GamePlayer[] gamePlayers = new GamePlayer[4];
		for (int i = 0; i < 4; i++) {
			gamePlayers[i] = playerDeque.poll();
			if (gamePlayers[i] == null) {
				// 不足4人时重新排队
				for (GamePlayer gamePlayer : gamePlayers) {
					if (gamePlayer == null)
						break;
					playerDeque.offerFirst(gamePlayer);
				}
				return;
			}
		}
		// 人数足够时开始游戏
		Table table = tableQueue.poll();
		if (table == null) {
			table = new Table(this);
		}
		table.getActor().start(gamePlayers);

	}

	/**
	 * 初始化
	 */
	public void init() {
		for (int i = 0; i < siteCo.getInitTableNum(); i++) {
			Table table = new Table(this);
			tableQueue.offer(table);
		}
	}

	public AtomicInteger getPlayerNum() {
		return playerNum;
	}

	public ConcurrentLinkedQueue<Table> getTableQueue() {
		return tableQueue;
	}

	public ConcurrentLinkedDeque<GamePlayer> getPlayerDeque() {
		return playerDeque;
	}

	public DelayQueue<Table> getWaitingTableQueue() {
		return waitingTableQueue;
	}

	public DelayQueue<GamePlayer> getDeadLineQueue() {
		return deadLineQueue;
	}
	
}