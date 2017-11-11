package com.su.core.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class GameContext {
	private Map<Integer, PlayerContext> playerContextMap = new ConcurrentHashMap<>();
	
	public void addPlayerContext(int id, PlayerContext playerContext) {
		playerContextMap.put(id, playerContext);
	}
	
	public void removePlayerContext(int id) {
		playerContextMap.remove(id);
	}
	
	public PlayerContext getPlayerContext(int id) {
		return playerContextMap.get(id);
	}
	
	public boolean containsPlayerContext(int id) {
		return playerContextMap.containsKey(id);
	}
}
