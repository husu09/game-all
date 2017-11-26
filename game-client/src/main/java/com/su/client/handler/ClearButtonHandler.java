package com.su.client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.su.client.core.ClientContext;

/**
 * 清空文本框
 * */
public class ClearButtonHandler implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		ClientContext.getInstance().getTextArea().setText("");
	}

}
