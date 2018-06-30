package com.su.core.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.su.config.ContestCo;
import com.su.core.context.PlayerContext;

public class ContestSite extends Site {
	
	private ContestCo contestCo;
	private List<GamePlayer> playerList;
	private Queue<Contest> contestQueue;
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
	 * */
	public synchronized void apply(PlayerContext playerContext) {
		if (playerContext.getGamePlayer() == null)
			new GamePlayer(playerContext);
		else if (playerList.contains(playerContext.getGamePlayer()))
			return;
		else if (playerContext.getGamePlayer().getState() != null)
			return;
		playerList.add(playerContext.getGamePlayer());
		getPlayerNum().incrementAndGet();
		// 人数已满开启比赛
		if (playerList.size() >= contestCo.getPlayerNum()) {
			Contest contest = contestQueue.poll();
			if (contest == null)
				contest = new Contest(this);
			// TODO
			playerList.clear();
			getPlayerNum().set(0);
		}
	}
	
	/**
	 * 取消报名
	 * */
	public synchronized void cancelApply(GamePlayer gamePlayer) {
		if (playerList.remove(gamePlayer))
			getPlayerNum().decrementAndGet();
		
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
	
	
	
	
}
