package com.su.server.obj.play.card;

import org.springframework.stereotype.Component;

import com.su.server.obj.play.CardType;

@Component
public class WangZha extends BaseCardProcessor  {

	@Override
	public boolean verify() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean compare() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CardType getCardType() {
		// TODO Auto-generated method stub
		return null;
	}

}
