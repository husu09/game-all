package com.su.client.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.googlecode.protobuf.format.JsonFormat;
import com.su.client.handler.LoginButtonHandler;

import io.netty.channel.ChannelHandlerContext;

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
	 */
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
	 */
	private JPanel panel;

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	/**
	 * 文本域
	 */
	private JTextArea textArea;

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	/**
	 * 参数文本框
	 */
	private List<JTextField> textFields = Collections.synchronizedList(new ArrayList<>());

	public List<JTextField> getTextFields() {
		return textFields;
	}

	public void setTextFields(List<JTextField> textFields) {
		this.textFields = textFields;
	}

	/**
	 * 用户名文本框
	 */
	private JTextField userNameTF;
	/**
	 * 地址文本框
	 */
	private JTextField hostTF;

	public JTextField getUserNameTF() {
		return userNameTF;
	}

	public void setUserNameTF(JTextField userNameTF) {
		this.userNameTF = userNameTF;
	}

	public JTextField getHostTF() {
		return hostTF;
	}

	public void setHostTF(JTextField hostTF) {
		this.hostTF = hostTF;
	}

	/**
	 * 连接对象
	 */
	private ChannelHandlerContext ctx;

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	
	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void write(MessageLiteOrBuilder msg) {
		ctx.writeAndFlush(msg);
	}

	/**
	 * 连接标识
	 */
	private CountDownLatch cdl = new CountDownLatch(1);

	public CountDownLatch getCdl() {
		return cdl;
	}

	/**
	 * 保存client数据
	 * */
	public void saveData(String host, String name) {
		try {
			Properties prop = new Properties();
			String path = LoginButtonHandler.class.getClassLoader().getResource(ClientConst.SAVE_FILE).getPath();
			prop.setProperty("host", host);
			prop.setProperty("name", name);
			FileOutputStream out = new FileOutputStream(path);
			prop.store(out, "client data");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取client数据
	 * */
	public Map<String, String> getData() {
		Map<String, String> map = new HashMap<>();
		try {
			Properties prop = new Properties();
			String path = LoginButtonHandler.class.getClassLoader().getResource("").getPath() + ClientConst.SAVE_FILE;
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			InputStream in = new FileInputStream(file);
			prop.load(in);
			map.put("host", prop.getProperty("host"));
			map.put("name", prop.getProperty("name"));
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	public void showMessage(String string) {
		textArea.append(string);
	}
	
	private JsonFormat jsonFormat = new JsonFormat();
	
	public void showMessage(MessageLite messageLite) {
		String jsonStr = FormatUtil.formatJson(jsonFormat.printToString((Message) messageLite));
		jsonStr = messageLite.getClass().getSimpleName() + jsonStr + "\n";
		showMessage(jsonStr);
	}

}
