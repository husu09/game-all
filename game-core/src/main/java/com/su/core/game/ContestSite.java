package com.su.core.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.su.config.ContestCo;
import com.su.core.context.PlayerContext;
import com.su.core.game.enums.SiteType;

public class ContestSite extends Site {
	/**
	 * 配置
	 * */
	private ContestCo contestCo;
	/**
	 * 报名的玩家
	 * */
	private List<GamePlayer> playerList;
	/**
	 * 空闲的比赛
	 * */
	private Queue<Contest> contestQueue;
	/**
	 * 排名奖励的人数
	 * */
	private int rewardCount;
	
	public ContestSite(ContestCo contestCo, int rewardCount) {
		this.contestCo = contestCo;
		this.rewardCount = rewardCount;
		contestQueue = new LinkedList<>();
		playerList = new ArrayList<>(contestCo.getPlayerNum());
		for (int i = 0; i < contestCo.getContestNum(); i ++){
			contestQueue.offer(new Contest(this));
		}
	}
	
	/**
	 * 报名
	 * @param playerContext
	 * @return 游戏开始 true、未开始 false
	 */
	public synchronized boolean apply(PlayerContext playerContext) {
		if (playerContext.getGamePlayer() == null)
			new GamePlayer(playerContext);
		else if (playerList.contains(playerContext.getGamePlayer()))
			return false;
		else if (playerContext.getGamePlayer().getState() != null)
			return false;
		playerList.add(playerContext.getGamePlayer());
		playerNum ++;
		// 人数已满开启比赛
		if (playerList.size() >= contestCo.getPlayerNum()) {
			Contest contest = contestQueue.poll();
			if (contest == null)
				contest = new Contest(this);
			contest.getActor().setGamePlayer(playerList);
			contest.getActor().start();
			playerList.clear();
			playerNum = 0;
			return true;
		}
		return false;
	}
	
	/**
	 * 取消报名
	 * */
	public synchronized void cancelApply(GamePlayer gamePlayer) {
		if (playerList.remove(gamePlayer))
			playerNum--;
		
	}
	
	/**
	 * 获取配置
	 * */
	public ContestCo getContestCo() {
		return contestCo;
	}
	
	/**
	 * 奖励个数
	 * */
	public int getRewardCount() {
		return rewardCount;
	}

	@Override
	public SiteType getSiteType() {
		return SiteType.CONTEST;
	}
	
	
	
	
}
