package com.su.core.akka;

import com.su.core.gambling.GamePlayer;
import com.su.core.gambling.Table;

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
	public void draw(GamePlayer player, int cardType, int[] indexs) {
		table.draw(player, cardType, indexs);
	}

	@Override
	public void check(GamePlayer player) {
		table.check(player);
	}

	@Override
	public void ready(GamePlayer player) {
		table.ready(player);
	}

	@Override
	public void exit(GamePlayer player) {
		table.exit(player);
	}

	@Override
	public void reconn(GamePlayer player) {
		table.reconnect(player);
	}

	@Override
	public void doWaitTable() {
		table.doWaitTable();
	}

	@Override
	public void doWaitGamePlayer(GamePlayer player) {
		table.doWaitGamePlayer(player);
	}

	@Override
	public void setIsAuto(GamePlayer player, int isAuto) {
		table.setIsAuto(player, isAuto);
	}
	
}
