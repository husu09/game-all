package com.su.client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.su.client.ClientStart;

/**
 * 生成 proto 参数输入框
 * */
public class ProtoButtonHandler implements ActionListener {
	
	private JPanel p;
	
	public ProtoButtonHandler(JPanel p) {
		this.p = p;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		p.setVisible(false);
		p.removeAll();
		JButton but = (JButton) e.getSource();
		Class<?> c = ClientStart.getClass(but.getText());
		Field[] fields = c.getDeclaredFields();
		for (Field f : fields) {
			if (f.getName().endsWith("_") && !f.getName().equals("bitField0_")) {
				JPanel r1p = new JPanel();
				JLabel r1l = new JLabel(f.getName().substring(0, f.getName().length() - 1));
				JTextField r1t = new JTextField(20);
				System.out.println(f.getType());
				System.out.println(f.getGenericType());
				r1p.add(r1l);
				r1p.add(r1t);
				p.add(r1p);
			}
		}
		p.setVisible(true);
	}

}
