package com.su.client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JTextField;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.su.client.ClientContext;
import com.su.client.NettyClient;
import com.su.core.util.ApplicationContextUtil;
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
		Properties prop = new Properties();
		InputStream in = LoginButtonHandler.class.getClassLoader().getResourceAsStream("data.properties");
		try {
			prop.load(in);
			prop.setProperty("host", hostTF.getText());
			prop.setProperty("name", userNameTF.getText());
			in.close();

			AnnotationConfigApplicationContext context = (AnnotationConfigApplicationContext) ApplicationContextUtil
					.getApplicationContext();
			// 启动服务
			NettyClient client = context.getBean(NettyClient.class);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						client.start(arr[0], Integer.parseInt(arr[1]));
					} catch (Exception e) {
						System.out.println("启动netty客户端失败！");
						e.printStackTrace();
					}
				}
			}, "netty-client").start();

			// 等待连接完成
			ctx.getCdl().await();
			ctx.write(LoginReq.newBuilder().setName(userNameTF.getText()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

}
