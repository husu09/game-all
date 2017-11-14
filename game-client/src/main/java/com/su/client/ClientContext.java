package com.su.client;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.google.protobuf.MessageLite;

public class ClientContext {

	private static ClientContext instance = new ClientContext();

	private ClientContext() {

	}

	public static ClientContext getInstance() {
		return instance;
	}

	/**
	 * 当前选中的协议
	 */
	private volatile MessageLite selectMessageLite;

	public MessageLite getSelectMessageLite() {
		return selectMessageLite;
	}

	public void setSelectMessageLite(MessageLite selectMessageLite) {
		this.selectMessageLite = selectMessageLite;
	}
	
	/**
	 * <协议名称,<属性名，类型>>
	 * */
	private Map<String, Map<String, Integer>> map = new HashMap<>();
	
	public void addProperty(String messageName, String propertyName, int propertyType) {
		if (map.get(messageName) == null) {
			map.put(messageName, new HashMap<>(5));
		}
		map.get(messageName).put(propertyName, propertyType);
	}
	
	public int getProperty(String messageName, String propertyName) {
		return map.get(messageName).get(propertyName);
	}
	
	/**
	 * 参数面板
	 * */
	private JPanel panel;

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
	
	/**
	 * 文本域
	 * */
	private JTextArea textArea;

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}
	
	
}
