package com.su.server.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.su.common.po.Player;
import com.su.common.util.StringUtil;
import com.su.core.action.Action;
import com.su.proto.LoginMsg.Login;
import com.su.proto.LoginMsg.Login_;
import com.su.server.context.GameContext;
import com.su.server.context.PlayerContext;
import com.su.server.event.GameEventDispatcher;
import com.su.server.service.LoginService;
import com.su.server.service.PlayerService;

@Controller
public class LoginControl {

	Logger logger = LoggerFactory.getLogger(LoginControl.class);

	@Autowired
	private GameContext gameContext;

	@Autowired
	private LoginService loginService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private GameEventDispatcher gameEventDispatcher;

	/**
	 * 登录
	 */
	@Action(mustLogin = false)
	public void login(PlayerContext playerContext, Login req) {
		if (StringUtil.isNone(req.getAccount()) || StringUtil.isNone(req.getName())) {
			logger.error("参数错误{},{}", req.getAccount(), req.getName());
			return;
		}

		long playerId = 0;
		if (loginService.containsIdCacheByName(req.getAccount())) {
			playerId = loginService.getIdCacheByName(req.getAccount());
		} else {
			// 创建用户
			Player player = new Player();
			player.setAccount(req.getAccount());
			player.setName(req.getName());
			playerId = playerService.createPlayer(player);
			loginService.addIdCacheByName(req.getAccount(), playerId);
		}
		if (playerId == 0) {
			playerContext.sendError(10002);
			return;
		}
		Player player = playerService.getPlayerById(playerId);
		if (player == null) {
			playerContext.sendError(10001);
			return;
		}
		playerContext.handleLogin(player);
		gameContext.getPlayerContextMap().put(player.getId(), playerContext);
		gameEventDispatcher.login(playerContext);
		Login_.Builder resp = Login_.newBuilder();
		resp.setPlayer(playerService.serializePlayer(player));
		playerContext.write(resp);

		loginService.addPlayerCache(player);
	}

}
