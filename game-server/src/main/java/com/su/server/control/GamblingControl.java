package com.su.server.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.su.common.constant.GamblingConst;
import com.su.config.BagCo;
import com.su.core.action.Action;
import com.su.core.context.PlayerContext;
import com.su.core.gambling.GamePlayer;
import com.su.core.gambling.Site;
import com.su.core.gambling.enums.PlayerState;
import com.su.excel.mapper.BagMapper;
import com.su.msg.GamblingMsg.Double;
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
		if (site == null)
			playerContext.sendError(1002);
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
		if (gamePlayer.getState() != PlayerState.OPERATE) {
			playerContext.sendError(3002);
			return;
		}
		// 加倍
		boolean result = gamePlayer.getTable().getActor().doubles(gamePlayer, bagCo.getEffectNum());
		// 加倍失败返还道具
		if (!result) {
			bagService.addItem(playerContext, GamblingConst.DOUBLES_ITEM, 0);
		}
		
	}
}
