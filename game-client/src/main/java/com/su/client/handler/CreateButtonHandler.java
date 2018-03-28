package com.su.client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import com.su.client.core.ClientContext;
import com.su.client.core.NettyUtil;

/**
 * 创建账号
 */
public class CreateButtonHandler implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

		ClientContext clientCtx = ClientContext.getInstance();
		JTextField hostTF = clientCtx.getHostTF();
		String[] arr = hostTF.getText().split(":");
		if (arr.length < 2) {
			System.out.println("服务器地址错误！");
			return;
		}
		JTextField userNameTF = clientCtx.getUserNameTF();
		if (userNameTF.getText().trim().equals("")) {
			System.out.println("用户名不能为空");
			return;
		}
		// 保存数据
		clientCtx.saveData(hostTF.getText(), userNameTF.getText());
		if (ClientContext.getInstance().getCtx() == null) {
			NettyUtil.start(arr[0], Integer.parseInt(arr[1]));
		}
		//clientCtx.write(RegisterReq.newBuilder().setName(userNameTF.getText()).build());

	}

}
