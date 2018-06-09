package com.su.core.akka;

import com.su.core.gambling.GamePlayer;
import com.su.core.gambling.Table;
import com.su.core.gambling.enums.CardType;

public class TableActorImpl implements TableActor {
	
	private Table table;
	
	public TableActorImpl(Table table) {
		this.table = table;
	}
	
	@Override
	public void setPlayers(GamePlayer[] players) {
		table.setPlayers(players);
	}
	
	@Override
	public void deal() {
		table.deal();
	}

	@Override
	public boolean doubles(GamePlayer player, int multiple) {
		return table.doubles(player, multiple);
	}

	@Override
	public void call(GamePlayer player,int callType, int index) {
		table.call(player, callType, index);
	}

	@Override
	public void draw(GamePlayer player, CardType cardType, int[] index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void check(GamePlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ready(GamePlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exit(GamePlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reconn(GamePlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doWaitTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doWaitGamePlayer(GamePlayer player) {
		// TODO Auto-generated method stub
		
	}

	
	
}
