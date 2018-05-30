package com.su.core.gambling.card;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.su.core.gambling.enums.CardType;

@Component
public abstract class BaseCardProcessor implements CardProcessor{
	
	@PostConstruct
	public void initialize(){ 
		
		
	}
	
	public abstract CardType getCardType();
	
}
