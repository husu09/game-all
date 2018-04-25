package com.su.server.obj.play.card;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.su.server.constant.ErrCode;
import com.su.server.obj.play.Card;
import com.su.server.obj.play.CardType;

@Component
public abstract class BaseCardProcessor implements CardProcessor{
	
	@PostConstruct
	public void initialize(){ 
		
		
	}
	
	public abstract CardType getCardType();
	
}
