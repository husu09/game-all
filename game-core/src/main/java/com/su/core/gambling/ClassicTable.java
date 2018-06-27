package com.su.core.gambling;

import com.su.core.gambling.enums.CallType;
import com.su.core.gambling.enums.CardType;
import com.su.core.gambling.enums.Team;
import com.su.msg.GamblingMsg.TableResult_.Builder;

public class ClassicTable extends MatchTable {

	public ClassicTable(MatchRoom room) {
		super(room);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void beforDeal() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void callAfter(GamePlayer playe, CallType callType, Card card) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawAfter(GamePlayer player, CardType cardType, Card[] cards) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeClose(Team winTeam) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doClose(Builder builder, Team winTeam, int redMultiple, int blueMultiple) {
		// TODO Auto-generated method stub
		
	}

	
}
