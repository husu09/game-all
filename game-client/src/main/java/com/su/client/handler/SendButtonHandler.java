package com.su.client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JTextField;

import com.google.protobuf.MessageLite;
import com.su.client.core.ClientConst;
import com.su.client.core.ClientContext;
import com.su.client.core.ClientUtil;

/**
 * 发送消息
 */
public class SendButtonHandler implements ActionListener {
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (ClientContext.getInstance().getCtx() == null) {
			ClientContext.getInstance().showMessage("请先登录！\n");
			return;
		}
		// 组装协议信息
		MessageLite messageLite = ClientContext.getInstance().getSelectMessageLite();
		List<JTextField> textFields = ClientContext.getInstance().getTextFields();
		try {
			Object builder = messageLite.getClass().getMethod("newBuilder").invoke(null);
			for (JTextField tf : textFields) {
				int type = ClientContext.getInstance().getProperty(messageLite.getClass().getSimpleName(),
						tf.getName());
				if (type == ClientConst.INT_TYPE) {
					int value = 0;
					if (!tf.getText().trim().equals("")) {
						value = Integer.parseInt(tf.getText());
					}
					builder.getClass().getMethod("set" + ClientUtil.upperFirstCharacter(tf.getName()), int.class)
							.invoke(builder, value);
				} else if (type == ClientConst.INTS_TYPE) {
					String[] arr = tf.getText().split(",");
					for (String s : arr) {
						int value = 0;
						if (!s.trim().equals("")) {
							value = Integer.parseInt(s);
						}
						builder.getClass().getMethod("add" + ClientUtil.upperFirstCharacter(tf.getName()), int.class)
								.invoke(builder, value);
					}
				} else if (type == ClientConst.STRING_TYPE) {
					builder.getClass().getMethod("set" + ClientUtil.upperFirstCharacter(tf.getName()), String.class)
							.invoke(builder, tf.getText());
				} else if (type == ClientConst.STRINGS_TYPE) {
					String[] arr = tf.getText().split(",");
					for (String s : arr) {
						builder.getClass().getMethod("add" + ClientUtil.upperFirstCharacter(tf.getName()), String.class)
						.invoke(builder, s);
					}
				}
			}
			MessageLite ml = (MessageLite) builder.getClass().getMethod("build").invoke(builder);
			// 发送消息
			ClientContext.getInstance().write(ml);
			ClientContext.getInstance().showMessage(ml);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}

}
