package com.su.server.context;

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

	public void addPlayerContext(long id, PlayerContext playerContext) {
		playerContextMap.put(id, playerContext);
	}

	public void removePlayerContext(long id) {
		playerContextMap.remove(id);
	}

	public PlayerContext getPlayerContext(long id) {
		return playerContextMap.get(id);
	}

	public boolean containsPlayerContext(long id) {
		return playerContextMap.containsKey(id);
	}

	public boolean isStopping() {
		return stopping;
	}

	public void setStopping(boolean stopping) {
		this.stopping = stopping;
	}
		
}
