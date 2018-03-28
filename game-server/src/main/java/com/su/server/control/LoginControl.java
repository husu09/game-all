package com.su.server.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.su.common.po.Player;
import com.su.core.action.Action;
import com.su.core.context.GameContext;
import com.su.core.context.PlayerContext;
import com.su.proto.LoginProto.LoginReq;
import com.su.proto.LoginProto.LoginResp;
import com.su.proto.LoginProto.LoginResp.Builder;
import com.su.proto.PlayerProto.PlayerDataPro;
import com.su.server.service.AccountService;

@Controller
public class LoginControl {
	
	@Autowired
	private GameContext gameContext;
	
	@Autowired
	private AccountService accountService;
	
	/**
	 * 登录
	 * */
	@Action(mustLogin = false)
	public void login(PlayerContext playerContext, LoginReq req) {
		Player player = accountService.queryPlayerByName(req.getName());
		if (player == null) {
			playerContext.sendError("", "未注册");
			return;
		}
		gameContext.addPlayerContext(player.getId(), playerContext);
		
		PlayerDataPro.Builder playerData = PlayerDataPro.newBuilder();
		playerData.setId(player.getId());
		playerData.setName(player.getName());
		
		Builder resp = LoginResp.newBuilder();
		resp.setPlayerData(playerData);
		playerContext.write(LoginResp.newBuilder());
		
	}
	
}
