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
		// 增加物品
		if (cmd.getCmd() == 10000) {
			Item item = new Item();
			item.setType(cmd.getType());
			item.setSysId(cmd.getSysId());
			item.setCount(cmd.getCount());
			bagService.addItem(playerContext, item, 1000);
		// 扣除物品
		} else if (cmd.getCmd() == 10001) {
			Item item = new Item();
			item.setType(cmd.getType());
			item.setSysId(cmd.getSysId());
			item.setCount(cmd.getCount());
			bagService.eddItem(playerContext, item, 2000);
		}
	}
}
