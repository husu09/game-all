package com.su.server.obj.play.card;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.su.server.obj.play.CardType;

@Component
public class CardProcessorManager {
	
	private Map<CardType, CardProcessor> cardProcessor = new HashMap<>();

	public Map<CardType, CardProcessor> getCardProcessor() {
		return cardProcessor;
	}
	
}
