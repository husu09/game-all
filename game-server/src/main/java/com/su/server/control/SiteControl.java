package com.su.server.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.su.common.constant.GamblingConst;
import com.su.config.BagCo;
import com.su.core.action.Action;
import com.su.core.context.PlayerContext;
import com.su.core.game.GamePlayer;
import com.su.excel.mapper.BagConf;
import com.su.msg.GamblingMsg.Auto;
import com.su.msg.GamblingMsg.Call;
import com.su.msg.GamblingMsg.Call_;
import com.su.msg.GamblingMsg.Double;
import com.su.msg.GamblingMsg.Double_;
import com.su.msg.GamblingMsg.Draw;
import com.su.msg.GamblingMsg.Draw_;
import com.su.msg.GamblingMsg.Match;
import com.su.msg.GamblingMsg.Quit;
import com.su.msg.GamblingMsg.Quit_;
import com.su.msg.GamblingMsg.Ready;
import com.su.msg.GamblingMsg.Ready_;
import com.su.msg.GamblingMsg.Reconn;
import com.su.server.service.ResourceService;
import com.su.server.service.MatchSiteService;

@Controller
public class SiteControl {

	@Autowired
	private MatchSiteService siteService;
	@Autowired
	private ResourceService resouceService;
	@Autowired
	private BagConf bagMapper;

	/**
	 * 开始匹配
	 */
	@Action
	public void match(PlayerContext playerContext, Match req) {/*
		if (playerContext.getGamePlayer() != null && playerContext.getGamePlayer().getState() != null){
			playerContext.sendError(3002);
			return;
		}
		Site site = siteService.getSiteMap().get(req.getSiteId());
		if (site == null) {
			playerContext.sendError(1002);
			return;
		}
		
		site.addPlayerToMatch(playerContext, false);
		playerContext.write(Match_.newBuilder());
	*/}

	/**
	 * 加倍
	 */
	@Action
	public void doubles(PlayerContext playerContext, Double req) {
		// 获取道具配置
		BagCo bagCo = bagMapper.get(GamblingConst.DOUBLES_ITEM.getSysId());
		if (bagCo == null) {
			playerContext.sendError(1004);
			return;
		}
		// 游戏用户检测
		GamePlayer gamePlayer = playerContext.getGamePlayer();
		if (gamePlayer == null) {
			playerContext.sendError(3001);
			return;
		}
		// 扣除道具
		if (!resouceService.edd(playerContext, GamblingConst.DOUBLES_ITEM, 2001)) {
			playerContext.sendError(1003, GamblingConst.DOUBLES_ITEM.getType(), GamblingConst.DOUBLES_ITEM.getSysId());
			return;
		}
		// 加倍
		boolean result = gamePlayer.getTable().getActor().doubles(gamePlayer, bagCo.getEffectNum());
		// 加倍失败返还道具
		if (!result) {
			resouceService.add(playerContext, GamblingConst.DOUBLES_ITEM, 10001);
		}
		playerContext.write(Double_.newBuilder());
	}

	/**
	 * 叫牌
	 */
	@Action
	public void call(PlayerContext playerContext, Call req) {
		// 游戏用户检测
		GamePlayer gamePlayer = playerContext.getGamePlayer();
		if (gamePlayer == null) {
			playerContext.sendError(3001);
			return;
		}
		gamePlayer.getTable().getActor().call(gamePlayer, req.getCallType(), req.getCardIndex());
		playerContext.write(Call_.newBuilder());
	}

	/**
	 * 出牌
	 */
	@Action
	public void draw(PlayerContext playerContext, Draw req) {
		// 游戏用户检测
		GamePlayer gamePlayer = playerContext.getGamePlayer();
		if (gamePlayer == null) {
			playerContext.sendError(3001);
			return;
		}
		int[] indexs = new int[req.getCardIndexsCount()];
		for (int i = 0; i < req.getCardIndexsCount(); i++)
			indexs[i] = req.getCardIndexs(i);
		gamePlayer.getTable().getActor().draw(gamePlayer, req.getCardType(), indexs);
		playerContext.write(Draw_.newBuilder());
	}

	/**
	 * 托管
	 */
	@Action
	public void auto(PlayerContext playerContext, Auto req) {/*
		// 游戏用户检测
		GamePlayer gamePlayer = playerContext.getGamePlayer();
		if (gamePlayer == null) {
			playerContext.sendError(3001);
			return;
		}
		gamePlayer.getTable().getActor().setIsAuto(gamePlayer, req.getIsAuto());
		playerContext.write(Auto_.newBuilder());
	*/}

	/**
	 * 准备
	 */
	@Action
	public void ready(PlayerContext playerContext, Ready req) {
		// 游戏用户检测
		GamePlayer gamePlayer = playerContext.getGamePlayer();
		if (gamePlayer == null) {
			playerContext.sendError(3001);
			return;
		}
		gamePlayer.getTable().getActor().ready(gamePlayer);
		playerContext.write(Ready_.newBuilder());
	}

	/**
	 * 退出
	 */
	@Action
	public void quit(PlayerContext playerContext, Quit req) {
		// 游戏用户检测
		GamePlayer gamePlayer = playerContext.getGamePlayer();
		if (gamePlayer == null) {
			playerContext.sendError(3001);
			return;
		}
		gamePlayer.getTable().getActor().exit(gamePlayer);
		playerContext.write(Quit_.newBuilder());
	}

	/**
	 * 重连
	 */
	@Action
	public void Reconn(PlayerContext playerContext, Reconn req) {/*
		// 游戏用户检测
		GamePlayer gamePlayer = playerContext.getGamePlayer();
		if (gamePlayer == null) {
			playerContext.sendError(3001);
			return;
		}
		gamePlayer.getTable().getActor().reconn(gamePlayer);
		playerContext.write(Reconn_.newBuilder());
	*/}
}
