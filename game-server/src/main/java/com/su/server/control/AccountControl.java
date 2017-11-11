package com.su.server.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.su.common.rmi.DataRmiService;
import com.su.core.action.Action;
import com.su.core.context.PlayerContext;
import com.su.proto.LoginProto.LoginReq;
import com.su.proto.LoginProto.LoginResp;
import com.su.proto.LoginProto.LoginResp.Builder;

@Controller
public class AccountControl {
	
	@Autowired
	private DataRmiService dataRmiService;
	
	@Action(mustLogin = false)
	public void login(PlayerContext playerContext, LoginReq req) {
		System.out.println("loginReq");
		playerContext.write(LoginResp.newBuilder());
		
	}
}
