package com.su.server.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.su.common.po.Player;
import com.su.core.action.Action;
import com.su.core.context.GameContext;
import com.su.core.context.PlayerContext;
import com.su.proto.LoginProto.LoginReq;
import com.su.proto.LoginProto.LoginResp;
import com.su.proto.LoginProto.RegisterReq;
import com.su.proto.LoginProto.RegisterResp;
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
		playerContext.write(LoginResp.newBuilder());
		
	}
	
	/**
	 * 注册
	 * */
	@Action(mustLogin = false)
	public void register(PlayerContext playerContext, RegisterReq req) {
		if (StringUtils.isEmpty(req.getName())) {
			playerContext.sendError("", "参数错误");
			return;
		}
		Player player = accountService.queryPlayerByName(req.getName());
		if (player != null) {
			playerContext.sendError("", "用户名重复");
			return;
		}
		accountService.createPlayer(req.getName());
		playerContext.write(RegisterResp.newBuilder());
	}
}
