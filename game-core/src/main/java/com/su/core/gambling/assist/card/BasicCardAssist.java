package com.su.core.gambling.assist.card;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.su.core.gambling.enums.CardType;

@Component
public abstract class BasicCardAssist implements CardAssist{
	
	@Autowired
	private CardAssistManager CardProcessorManager;
	
	@PostConstruct
	public void initialize(){ 
		CardProcessorManager.getCardProcessor().put(getCardType(), this);
	}
	
	public abstract CardType getCardType();
	
}
