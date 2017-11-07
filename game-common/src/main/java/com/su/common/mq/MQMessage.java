package com.su.common.mq;

public class MQMessage {
	/**
	 * 数据库操作
	 * */
	private MQOperator mqOperator;
	/**
	 * 对象类名
	 * */
	private String className;
	/**
	 * 对象数据
	 * */
	private String data;
	
	public MQOperator getMqOperator() {
		return mqOperator;
	}
	public void setMqOperator(MQOperator mqOperator) {
		this.mqOperator = mqOperator;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	
	
}
