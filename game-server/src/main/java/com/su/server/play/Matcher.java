package com.su.server.play;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.su.core.context.PlayerContext;

/**
 * 匹配器
 * */
public class Matcher {
	/**
	 * 玩家队列
	 * */
	private  ConcurrentLinkedQueue<GamePlayer> playerQueue = new ConcurrentLinkedQueue<>();
	/**
	 * 场所
	 * */
	private Site site;
	
	public Matcher(Site site) {
		this.site = site;
	}
	
	/**
	 * 加入玩家队列
	 * */
	
	
	
}
