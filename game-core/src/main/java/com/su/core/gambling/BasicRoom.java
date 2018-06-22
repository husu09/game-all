package com.su.core.gambling;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BasicRoom {
	/**
	 * 玩家人数
	 */
	private AtomicInteger playerNum = new AtomicInteger();
	/**
	 * 空闲牌桌队列
	 */
	private ConcurrentLinkedQueue<BasicTable> idleTableQueue = new ConcurrentLinkedQueue<>();
	/**
	 * 需要操作的牌桌队列
	 */
	private DelayQueue<BasicTable> waitTableQueue = new DelayQueue<>();
	/**
	 * 需要操作的玩家队列
	 */
	private DelayQueue<GamePlayer> waitGamePlayerQueue = new DelayQueue<>();
	
	public AtomicInteger getPlayerNum() {
		return playerNum;
	}
	
	public ConcurrentLinkedQueue<BasicTable> getIdleTableQueue() {
		return idleTableQueue;
	}

	public DelayQueue<BasicTable> getWaitTableQueue() {
		return waitTableQueue;
	}

	public DelayQueue<GamePlayer> getWaitGamePlayerQueue() {
		return waitGamePlayerQueue;
	}

	public void doWaitTable() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				BasicTable table = waitTableQueue.take();
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
