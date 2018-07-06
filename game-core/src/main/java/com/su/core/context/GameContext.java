package com.su.core.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * 存储所有在线用户
 */
@Component
public class GameContext {
	/**
	 * 是否正在关服
	 * */
	private volatile boolean stopping;
	
	private Map<Long, PlayerContext> playerContextMap = new ConcurrentHashMap<>();

	public Map<Long, PlayerContext> getPlayerContextMap() {
		return playerContextMap;
	}

	public boolean isStopping() {
		return stopping;
	}

	public void setStopping(boolean stopping) {
		this.stopping = stopping;
	}
		
}
