package com.su.server.control;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.su.common.po.Player;
import com.su.core.action.Action;
import com.su.core.context.GameContext;
import com.su.core.context.PlayerContext;
import com.su.core.event.GameEventDispatcher;
import com.su.proto.LoginProto.LoginReq;
import com.su.proto.LoginProto.LoginResp;
import com.su.server.constant.ErrCode;
import com.su.server.service.LoginService;
import com.su.server.service.PlayerService;

@Controller
public class LoginControl {

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
	public void login(PlayerContext playerContext, LoginReq req) {
		String name = req.getName();
		if (StringUtils.isBlank(name)) {
			playerContext.sendError(ErrCode.PLAYER_NAME_IS_EMPTY);
			return;
		}
		long playerId = 0;
		if (!loginService.containsIdCacheByName(name)) {
			// 创建用户
			playerId = playerService.createPlayer(name);
			loginService.addIdCacheByName(name, playerId);
		}
		if (playerId == 0) {
			playerId = loginService.getIdCacheByName(name);
		}
		Player player = playerService.getPlayerById(playerId);
		if (player == null) {
			playerContext.sendError(ErrCode.PLAYER_IS_NULL);
			return;
		}
		playerContext.handleLogin(player);
		gameContext.addPlayerContext(player.getId(), playerContext);
		gameEventDispatcher.login(playerContext);
		LoginResp.Builder resp = LoginResp.newBuilder();
		resp.setPlayerData(playerService.toPlayerDataPro(player));
		playerContext.write(resp);

		loginService.addPlayerCache(player);
	}

}
