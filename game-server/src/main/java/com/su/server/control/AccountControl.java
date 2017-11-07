package com.su.server.control;

import org.springframework.stereotype.Controller;

import com.su.core.action.Action;
import com.su.proto.LoginProto.LoginReq;

@Controller
public class AccountControl {
	
	@Action(mustLogin = false)
	public void login(Object temp, LoginReq req) {
		System.out.println("loginReq");
	}
}
