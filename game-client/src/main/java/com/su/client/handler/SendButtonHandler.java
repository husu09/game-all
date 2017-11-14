package com.su.client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.google.protobuf.MessageLite;
import com.su.client.ClientContext;
import com.su.proto.LoginProto.LoginReq;
import com.su.proto.LoginProto.LoginResp;
import com.su.proto.LoginProto.RegisterReq;

/**
 * 发送消息
 * */
public class SendButtonHandler implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// 组装协议信息
		MessageLite messageLite = ClientContext.getInstance().getSelectMessageLite();
		/*messageLite.getClass().getSimpleName();
		RegisterReq.newBuilder().setId(value);
		RegisterReq.newBuilder().addIds(value);
		RegisterReq.newBuilder().setName(value);
		RegisterReq.newBuilder().addResults(value)*/
		// 发送消息
	}

}
