package com.su.client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.google.protobuf.MessageLite;
import com.su.client.ClientConst;
import com.su.client.ClientContext;
import com.su.core.proto.ProtoContext;
import com.su.core.util.ApplicationContextUtil;

/**
 * 生成 proto 参数输入框
 */
public class ProtoButtonHandler implements ActionListener {


	@Override
	public void actionPerformed(ActionEvent e) {
		ClientContext.getInstance().getTextFields().clear();
		JPanel p = ClientContext.getInstance().getPanel();
		p.setVisible(false);
		p.removeAll();
		JButton but = (JButton) e.getSource();
		ProtoContext protoContext = ApplicationContextUtil.getApplicationContext().getBean(ProtoContext.class);
		MessageLite messageLite = protoContext.get(but.getText());
		ClientContext.getInstance().setSelectMessageLite(messageLite);
		Class<?> c = messageLite.getClass();
		Field[] fields = c.getDeclaredFields();
		for (Field f : fields) {
			if (f.getName().endsWith("_") && !f.getName().equals("bitField0_")) {
				JPanel r1p = new JPanel();
				String propertyName = f.getName().substring(0, f.getName().length() - 1);
				JLabel r1l = new JLabel(propertyName);
				JTextField r1t = new JTextField(20);
				r1t.setName(propertyName);
				ClientContext.getInstance().getTextFields().add(r1t);
				int propertyType = 0;
				if (f.getType().getName().equals("int")) {
					propertyType = ClientConst.INT_TYPE;
				} else if (f.getType().getName().equals("java.lang.Object")) {
					propertyType = ClientConst.STRING_TYPE;
				} else if (f.getType().getName().equals("java.util.List")) {
					propertyType = ClientConst.INTS_TYPE;
					r1t.setText(",,,");
				} else if (f.getType().getName().equals("com.google.protobuf.LazyStringList")) {
					propertyType = ClientConst.STRINGS_TYPE;
					r1t.setText(",,,");
				} else {
					System.out.println("未知的字段类型：" + propertyName);
					continue;
				}
				ClientContext.getInstance().addProperty(but.getText(), propertyName, propertyType);
				r1p.add(r1l);
				r1p.add(r1t);
				p.add(r1p);
			}
		}
		p.setVisible(true);
	}

}
