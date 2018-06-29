package com.su.core.game;

import com.su.config.ContestCo;

public class ContestSite extends Site {
	/**
	 * 牌桌
	 * */
	private ContestTable[] tables;
	/**
	 * 玩家
	 * */
	private GamePlayer[] players;
	/**
	 * 排名
	 * */
	private Integer[] rankings;
	/**
	 * 底分
	 * */
	private int baseScore;
	
	
	private ContestCo contestCo;
	
	public ContestSite(ContestCo contestCo, GamePlayer[] players) {
		this.contestCo = contestCo;
		
		for (int i = 0 ; i < contestCo.getTableNum(); i ++){
			
		}
	}
	
	/**
	 * 报名
	 * */
	public void apply() {
		
	}
	
	/**
	 * 取消报名
	 * */
	public void cancelApply() {
		
	}
}
