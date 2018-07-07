package com.su.core.game;

import com.su.core.game.enums.TableState;
import com.su.core.game.enums.Team;
import com.su.msg.TableMsg.TableResult_;

public class ContestTable  extends Table {

	private Contest contest;
	
	public ContestTable(Site room, Contest contest) {
		super(room);
		this.contest = contest;
	}

	@Override
	public void doClose(TableResult_.Builder builder, Team winTeam, int redMultiple, int blueMultiple) {
		builder.setBaseScore(contest.getBaseScore());
		for (GamePlayer otherPlayer : this.players) {
			int multiple = 0;
			if (otherPlayer.getTeam() == Team.RED)
				multiple = redMultiple;
			else
				multiple = blueMultiple;
			int addOrEddScore = multiple * contest.getBaseScore();
			if (otherPlayer.getTeam() == winTeam) {
				otherPlayer.setContestScore(otherPlayer.getContestScore() + addOrEddScore);
			}
			else {
				// 处理玩家比赛积分
				if (otherPlayer.getContestScore() < addOrEddScore)
				otherPlayer.setContestScore(0);
				else
					otherPlayer.setContestScore(otherPlayer.getContestScore() - addOrEddScore);
			}
		}
		// 清空牌桌
		this.clean();
		// 检测每轮结束
		contest.getActor().checkTableClose(this);
	}

	@Override
	public void doExit(GamePlayer gamePlayer) {
		// 设置退赛状态
		gamePlayer.setIsQuit(1);
		if (this.state != TableState.CLOSE)
			gamePlayer.setIsAuto(1);
	}

	@Override
	public void doWaitClose() {
		
	}

}
