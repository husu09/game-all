package com.su.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.google.protobuf.MessageLite;
import com.su.client.handler.CreateButtonHandler;
import com.su.client.handler.LoginButtonHandler;
import com.su.client.handler.ProtoButtonHandler;
import com.su.client.handler.SendButtonHandler;
import com.su.core.proto.ProtoContext;
import com.su.core.util.ApplicationContextUtil;

@Component
public class ClientUI {
	@Autowired
	private ProtoContext protoContext;
	@Autowired
	private NettyClient client;

	public void show() throws Exception {
		ClientContext ctx = ClientContext.getInstance();
		// 窗口
		JFrame frame = new JFrame();
		frame.setSize(1000, 700);
		frame.setLocationByPlatform(true);
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		frame.setLayout(new BorderLayout());
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				AnnotationConfigApplicationContext context = (AnnotationConfigApplicationContext) ApplicationContextUtil
						.getApplicationContext();
				client.stop();
				context.close();
				System.exit(0);
			}
		});
		// =========================
		// 左面板
		// =========================
		JPanel leftP = new JPanel();
		leftP.setLayout(new GridLayout(1, 2));
		leftP.setPreferredSize(new Dimension(460, 0));

		// 左左
		JPanel leftLeft = new JPanel();
		leftLeft.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		// 第一行
		JPanel r1p = new JPanel();
		JLabel ipL = new JLabel("地址");
		JButton createB = new JButton("创号");
		createB.addActionListener(new CreateButtonHandler());
		JTextField ipT = new JTextField(20);
		r1p.add(ipL);
		r1p.add(ipT);
		r1p.add(createB);
		leftLeft.add(r1p);
		ctx.setHostTF(ipT);

		// 第二行
		JPanel r2p = new JPanel();
		JLabel nameL = new JLabel("用户");
		JTextField nameT = new JTextField(20);
		JButton loginB = new JButton("登录");
		loginB.addActionListener(new LoginButtonHandler());
		r2p.add(nameL);
		r2p.add(nameT);
		r2p.add(loginB);
		leftLeft.add(r2p);
		ctx.setUserNameTF(nameT);

		// 第三行
		/*
		JPanel r3p = new JPanel();
		JLabel passwordL = new JLabel("密码");
		JTextField passwordT = new JTextField(20);
		JButton sendB = new JButton("发送");
		sendB.addActionListener(new SendButtonHandler());
		r3p.add(passwordL);
		r3p.add(passwordT);
		r3p.add(sendB);
		leftLeft.add(r3p);
		*/

		// 左下
		JPanel leftBelow = new JPanel();
		leftBelow.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		leftBelow.setPreferredSize(new Dimension(460 / 2, 550));
		leftLeft.add(leftBelow);

		leftP.add(leftLeft);

		// 左右
		JPanel leftRight = new JPanel();
		leftRight.setLayout(new GridLayout(1, 1));
		JPanel lrp = new JPanel();
		lrp.setLayout(new GridLayout(0, 1));
		JScrollPane lrsp = new JScrollPane(lrp);
		leftRight.add(lrsp);
		loadProto(lrp);
		ClientContext.getInstance().setPanel(leftBelow);

		leftP.add(leftRight);

		// =========================
		// 右面板
		// =========================
		JPanel rightP = new JPanel();
		rightP.setLayout(new BorderLayout());

		// 右上
		JPanel rightTop = new JPanel();
		rightTop.setLayout(new BorderLayout());
		JTextArea textA = new JTextArea(20, 20);
		JScrollPane scroll = new JScrollPane(textA);
		rightTop.add(scroll);
		rightP.add("Center", rightTop);
		ClientContext.getInstance().setTextArea(textA);

		// 右下
		JPanel rightBelow = new JPanel();
		JButton clearB = new JButton("清空");
		rightBelow.setLayout(new FlowLayout());
		rightBelow.add(clearB);
		rightP.add("South", rightBelow);

		frame.getContentPane().add("West", leftP);
		frame.getContentPane().add("Center", rightP);
		frame.setVisible(true);

	}

	/**
	 * 加载 proto 请求协议 UI
	 * 
	 * @param leftRight
	 *            显示协议的面板
	 * @param leftBelow
	 *            显示协议参数的面板
	 */
	private void loadProto(JComponent leftRight) {
		ProtoButtonHandler pbh = new ProtoButtonHandler();
		for (Entry<String, MessageLite> e : protoContext.all()) {
			if (e.getKey().contains("Resp"))
				continue;
			JButton tB = new JButton(e.getKey());
			tB.setPreferredSize(new Dimension(180, 25));
			tB.addActionListener(pbh);
			leftRight.add(tB);
		}
	}

}
