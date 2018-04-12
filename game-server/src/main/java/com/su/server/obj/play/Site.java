package com.su.server.obj.play;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.su.excel.co.SiteCo;
import com.su.excel.map.SiteConf;

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
	/**
	 * 配置
	 * */
	private SiteCo siteCo;
	
	public Site(SiteCo siteCo) {
		this.siteCo  = siteCo;
	}
	/**
	 * 初始化
	 * */
	public void init() {
		for (int i = 0; i < siteCo.getInitTableNum(); i ++) {
			
		}
	}
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
