package com.su.core.gambling.card;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.su.core.gambling.enums.CardType;

@Component
public class CardProcessorManager {
	
	private Map<CardType, CardProcessor> cardProcessor = new HashMap<>();

	public Map<CardType, CardProcessor> getCardProcessor() {
		return cardProcessor;
	}
	
}
