package com.su.common.obj.play.card;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.su.common.obj.play.CardType;

@Component
public abstract class BaseCardProcessor implements CardProcessor{
	
	@PostConstruct
	public void initialize(){ 
		
		
	}
	
	public abstract CardType getCardType();
	
}
