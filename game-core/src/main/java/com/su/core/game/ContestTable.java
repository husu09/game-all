package com.su.core.game;

import com.su.core.game.enums.Team;
import com.su.msg.GamblingMsg.TableResult_.Builder;

public class ContestTable  extends Table {

	public ContestTable(Site room) {
		super(room);
	}

	@Override
	public void doClose(Builder builder, Team winTeam, int redMultiple, int blueMultiple) {
		
	}

	@Override
	public void doExit(GamePlayer gamePlayer) {
		
	}

	@Override
	public void doWaitClose() {
		
	}

}
