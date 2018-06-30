package com.su.core.game;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.TreeMap;

import com.su.common.constant.GameConst;
import com.su.common.util.ArrayUtil;
import com.su.core.akka.AkkaContext;

public class Contest {
	/**
	 * 牌桌
	 */
	private Queue<Table> tableQueue;
	/**
	 * 玩家
	 */
	private List<GamePlayer> playerList;
	/**
	 * 排名
	 */
	private Map<GamePlayer, Integer> rankingMap;
	/**
	 * 底分
	 */
	private int baseScore;
	/**
	 * 牌桌数
	 */
	private int tableNum;

	private Contest actor;

	private ContestSite contestSite;

	public Contest(ContestSite contestSite) {
		this.actor = AkkaContext.createActor(Contest.class, Contest.class, this);
		this.contestSite = contestSite;
		this.playerList = new LinkedList<>();
		this.rankingMap = new TreeMap<>();
		// 初始化牌桌
		this.tableNum = this.contestSite.getContestCo().getPlayerNum() / GameConst.PLAYER_COUNT;
		this.tableQueue = new LinkedList<>();
		for (int i = 0; i < this.tableNum; i++)
			this.tableQueue.offer(new ContestTable(contestSite, this));

	}

	/**
	 * 设置玩家
	 */
	public void setGamePlayer(GamePlayer[] players) {
		for (int i = 0; i < players.length; i++)
			this.playerList.add(players[i]);
		start();
	}

	/**
	 * 开始比赛
	 */
	public void start() {
		GamePlayer[] tablePlayer = new GamePlayer[GameConst.PLAYER_COUNT];
		int index = 0;
		for (GamePlayer player : this.playerList) {
			tablePlayer[index++] = player;
			if (ArrayUtil.getCount(tablePlayer) >= GameConst.PLAYER_COUNT) {
				Table table = tableQueue.poll();
				table.getActor().setPlayers(tablePlayer);
				table.getActor().deal();
				ArrayUtil.clear(tablePlayer);
				index = 0;
			}

		}
	}

	/**
	 * 处理每轮结束
	 */
	public void checkTableClose() {
		if (this.tableQueue.size() < this.tableNum)
			return;
		this.baseScore += this.contestSite.getContestCo().getAddedScore();
		// 处理排名
		for (Iterator<GamePlayer> it = this.playerList.iterator(); it.hasNext();) {
			GamePlayer gamePlayer = it.next();
			this.rankingMap.put(gamePlayer, gamePlayer.getContestScore());
			// 淘汰玩家
			if (gamePlayer.getContestScore() < baseScore) {
				it.remove();
			}
		}
		// 是否结束
		if (this.playerList.size() < GameConst.PLAYER_COUNT) {
			// 发放奖励
			int i = 1;
			for (Entry<GamePlayer, Integer> e : this.rankingMap.entrySet()) {
				if (i > this.contestSite.getRewardCount())
					break;
				e.getKey().getPlayerContext().getActor().doContestClose(i);
				i++;
			}
		}
		// 开始下一轮
		start();
	}

	public Contest getActor() {
		return actor;
	}

}
