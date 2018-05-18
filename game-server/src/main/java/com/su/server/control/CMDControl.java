package com.su.server.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.su.common.obj.Item;
import com.su.core.action.Action;
import com.su.proto.CommonMsg.CMD;
import com.su.server.context.PlayerContext;
import com.su.server.service.BagService;

@Controller
public class CMDControl {
	
	@Autowired
	private BagService bagService;
	
	@Action
	public void cmd(PlayerContext playerContext, CMD cmd) {
		Item item = new Item();
		item.setType(cmd.getType());
		item.setSysId(cmd.getSysId());
		item.setCount(cmd.getCount());
		bagService.addItem(playerContext, item, 1000);
	}
}
