package com.su.server.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.su.common.constant.GamblingConst;
import com.su.config.BagCo;
import com.su.core.action.Action;
import com.su.core.context.PlayerContext;
import com.su.core.gambling.GamePlayer;
import com.su.core.gambling.Site;
import com.su.excel.mapper.BagMapper;
import com.su.msg.GamblingMsg.Auto;
import com.su.msg.GamblingMsg.Call;
import com.su.msg.GamblingMsg.Double;
import com.su.msg.GamblingMsg.Draw;
import com.su.msg.GamblingMsg.Quit;
import com.su.msg.GamblingMsg.Ready;
import com.su.msg.GamblingMsg.Reconn;
import com.su.msg.GamblingMsg.Start;
import com.su.server.service.BagService;
import com.su.server.service.GamblingService;

@Controller
public class GamblingControl {
	
	@Autowired
	private GamblingService gamblingService;
	@Autowired
	private BagService bagService;
	@Autowired
	private BagMapper bagMapper;
	
	/**
	 * 开始匹配
	 * */
	@Action
	public void start(PlayerContext playerContext, Start req) {
		Site site = gamblingService.getSiteMap().get(req.getSiteId());
		if (site == null) {
			playerContext.sendError(1002);
			return;
		}
		site.startMatch(playerContext);
	}
	
	/**
	 * 加倍
	 * */
	@Action
	public void doubles(PlayerContext playerContext, Double req) {
		// 获取道具配置
		BagCo bagCo = bagMapper.get(GamblingConst.DOUBLES_ITEM.getSysId());
		if (bagCo == null) {
			playerContext.sendError(1004);
			return;
		}
		// 扣除道具
		if(!bagService.eddItem(playerContext, GamblingConst.DOUBLES_ITEM, 0)) {
			playerContext.sendError(1003, GamblingConst.DOUBLES_ITEM.getType(),GamblingConst.DOUBLES_ITEM.getSysId());
			return;
		}
		// 游戏用户检测
		GamePlayer gamePlayer = playerContext.getGamePlayer();
		if (gamePlayer == null) {
			playerContext.sendError(3001);
			return;
		}
		// 加倍
		boolean result = gamePlayer.getTable().getActor().doubles(gamePlayer, bagCo.getEffectNum());
		// 加倍失败返还道具
		if (!result) {
			bagService.addItem(playerContext, GamblingConst.DOUBLES_ITEM, 0);
		}
	}
	
	/**
	 * 叫牌
	 * */
	@Action
	public void call(PlayerContext playerContext, Call req) {
		// 游戏用户检测
		GamePlayer gamePlayer = playerContext.getGamePlayer();
		if (gamePlayer == null) {
			playerContext.sendError(3001);
			return;
		}
		gamePlayer.getTable().getActor().call(gamePlayer, req.getCallType(), req.getCardIndex());
	}
	
	/**
	 * 出牌
	 * */
	@Action
	public void draw(PlayerContext playerContext, Draw req) {
		// 游戏用户检测
		GamePlayer gamePlayer = playerContext.getGamePlayer();
		if (gamePlayer == null) {
			playerContext.sendError(3001);
			return;
		}
		gamePlayer.getTable().getActor().draw(gamePlayer, req.getCardType(), req.getCardIndexsList());
	}
	
	/**
	 * 托管
	 * */
	@Action
	public void auto(PlayerContext playerContext, Auto req) {
		// 游戏用户检测
		GamePlayer gamePlayer = playerContext.getGamePlayer();
		if (gamePlayer == null) {
			playerContext.sendError(3001);
			return;
		}
		gamePlayer.getTable().getActor().call(gamePlayer, req.getCallType(), req.getCardIndex());
	}
	
	/**
	 * 准备
	 * */
	@Action
	public void ready(PlayerContext playerContext, Ready req) {
		// 游戏用户检测
		GamePlayer gamePlayer = playerContext.getGamePlayer();
		if (gamePlayer == null) {
			playerContext.sendError(3001);
			return;
		}
		gamePlayer.getTable().getActor().call(gamePlayer, req.getCallType(), req.getCardIndex());
	}
	
	/**
	 * 退出
	 * */
	@Action
	public void quit(PlayerContext playerContext, Quit req) {
		// 游戏用户检测
		GamePlayer gamePlayer = playerContext.getGamePlayer();
		if (gamePlayer == null) {
			playerContext.sendError(3001);
			return;
		}
		gamePlayer.getTable().getActor().call(gamePlayer, req.getCallType(), req.getCardIndex());
	}
	
	/**
	 * 重连
	 * */
	@Action
	public void Reconn(PlayerContext playerContext, Reconn req) {
		// 游戏用户检测
		GamePlayer gamePlayer = playerContext.getGamePlayer();
		if (gamePlayer == null) {
			playerContext.sendError(3001);
			return;
		}
		gamePlayer.getTable().getActor().call(gamePlayer, req.getCallType(), req.getCardIndex());
	}
}
