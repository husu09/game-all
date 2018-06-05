package com.su.server.control;

import org.springframework.stereotype.Controller;

import com.su.core.action.Action;
import com.su.core.context.PlayerContext;
import com.su.msg.GamblingMsg.Start;

@Controller
public class GamblingControl {
	
	/**
	 * 开始匹配
	 * */
	@Action
	public void start(PlayerContext playerContext, Start req) {
		
	}
}
