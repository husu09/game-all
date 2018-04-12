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
	private AtomicInteger playerNum = new AtomicInteger();
	/**
	 * 匹配器
	 * */
	private Matcher matcher = new Matcher(this);
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
		init();
	}
	
	/**
	 * 初始化
	 * */
	public void init() {
		for (int i = 0; i < siteCo.getInitTableNum(); i ++) {
			Table table = new Table();
			tableQueue.offer(table);
		}
	}

	public AtomicInteger getPlayerNum() {
		return playerNum;
	}

	public Matcher getMatcher() {
		return matcher;
	}

	public ConcurrentLinkedQueue<Table> getTableQueue() {
		return tableQueue;
	}

	public SiteCo getSiteCo() {
		return siteCo;
	}
	
	
	
	
	
	
}
