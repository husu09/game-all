package com.su.server.play;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 场所
 * */
public class Site {
	/**
	 * 玩家数
	 * */
	private AtomicInteger playerNum;
	/**
	 * 匹配器
	 * */
	private Matcher matcher;
	/**
	 * 牌桌队列
	 * */
	private ConcurrentLinkedQueue<Table> tableQueue = new ConcurrentLinkedQueue<>();
	
	public AtomicInteger getPlayerNum() {
		return playerNum;
	}
	public void setPlayerNum(AtomicInteger playerNum) {
		this.playerNum = playerNum;
	}
	public Matcher getMatcher() {
		return matcher;
	}
	public void setMatcher(Matcher matcher) {
		this.matcher = matcher;
	}
	public ConcurrentLinkedQueue<Table> getTableQueue() {
		return tableQueue;
	}
	public void setTableQueue(ConcurrentLinkedQueue<Table> tableQueue) {
		this.tableQueue = tableQueue;
	}
	
	
	
}
