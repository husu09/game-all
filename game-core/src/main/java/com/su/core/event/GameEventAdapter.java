package com.su.core.event;

import javax.annotation.PostConstruct;

public class GameEventAdapter implements GameEvent {
	
	@PostConstruct
	private void init() {
		GameEventDispatcher.register(this);
	}
}
