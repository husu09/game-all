package com.su.server.obj.play;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.su.excel.co.SiteCo;
import com.su.server.context.PlayerContext;

/**
 * 场所
 */
public class Site {

	/**
	 * 玩家数
	 */
	private AtomicInteger playerNum = new AtomicInteger();
	/**
	 * 空闲牌桌队列
	 */
	private ConcurrentLinkedQueue<Table> tableQueue = new ConcurrentLinkedQueue<>();
	/**
	 * 游戏中的牌桌队列
	 */
	private ConcurrentLinkedQueue<Table> playingTableQueue = new ConcurrentLinkedQueue<>();
	/**
	 * 等待中的牌桌队列
	 */
	private ConcurrentLinkedQueue<Table> waitingTableQueue = new ConcurrentLinkedQueue<>();
	/**
	 * 玩家队列
	 */
	private ConcurrentLinkedDeque<GamePlayer> playerDeque = new ConcurrentLinkedDeque<>();
	/**
	 * 配置
	 */
	private SiteCo siteCo;

	public Site(SiteCo siteCo) {
		this.siteCo = siteCo;
		init();
	}

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
		table.getActor().initTable(gamePlayers);

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

	public ConcurrentLinkedQueue<Table> getPlayingTableQueue() {
		return playingTableQueue;
	}

	public ConcurrentLinkedQueue<Table> getWaitingTableQueue() {
		return waitingTableQueue;
	}

	public ConcurrentLinkedDeque<GamePlayer> getPlayerDeque() {
		return playerDeque;
	}
	
}
