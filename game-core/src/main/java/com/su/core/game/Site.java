package com.su.core.game;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Site {
	/**
	 * 玩家人数
	 */
	private AtomicInteger playerNum = new AtomicInteger();
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
	
	public AtomicInteger getPlayerNum() {
		return playerNum;
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
	

}
