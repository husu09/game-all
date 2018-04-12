package com.su.server.event;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.su.server.context.PlayerContext;

@Component
public class GameEventDispatcher implements GameEvent {
	
	private List<GameEvent> gameEventHandlers = new ArrayList<>();
	
	
	/**
	 * 注册到事件分发器
	 * */
	public void register(GameEvent gameEvent) {
		gameEventHandlers.add(gameEvent);
	}

	@Override
	public void serverStart() {
		for (GameEvent gameEvent : gameEventHandlers) 
			gameEvent.serverStart();
	}

	@Override
	public void serverStop() {
		for (GameEvent gameEvent : gameEventHandlers) 
			gameEvent.serverStop();
	}

	@Override
	public void dailyReset() {
		for (GameEvent gameEvent : gameEventHandlers) 
			gameEvent.dailyReset();
	}

	@Override
	public void login(PlayerContext playerContext) {
		for (GameEvent gameEvent : gameEventHandlers) 
			gameEvent.login(playerContext);
	}

	@Override
	public void logout(PlayerContext playerContext) {
		for (GameEvent gameEvent : gameEventHandlers) 
			gameEvent.logout(playerContext);
	}

	@Override
	public void dailyReset(PlayerContext playerContext) {
		for (GameEvent gameEvent : gameEventHandlers) 
			gameEvent.dailyReset(playerContext);
	}
	

	
}
