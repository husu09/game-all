package com.su.client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import com.su.client.core.ClientContext;
import com.su.client.core.NettyUtil;
import com.su.proto.LoginProto.LoginReq;

/**
 * 登录
 */
public class LoginButtonHandler implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		ClientContext ctx = ClientContext.getInstance();
		JTextField hostTF = ctx.getHostTF();
		String[] arr = hostTF.getText().split(":");
		if (arr.length < 2) {
			System.out.println("服务器地址错误！");
			return;
		}
		JTextField userNameTF = ctx.getUserNameTF();
		if (userNameTF.getText().trim().equals("")) {
			System.out.println("用户名不能为空");
			return;
		}
		// 保存数据
		ctx.saveData(hostTF.getText(), userNameTF.getText());
		if (ClientContext.getInstance().getCtx() == null) {
			NettyUtil.start(arr[0], Integer.parseInt(arr[1]));
		}
		ctx.write(LoginReq.newBuilder().setName(userNameTF.getText()).build());

	}

}
