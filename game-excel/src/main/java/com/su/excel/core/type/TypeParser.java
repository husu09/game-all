package com.su.excel.core.type;

public interface TypeParser<T> {
	/**
	 * 解析方法
	 * */
	public T parser(String value);
	
	/**
	 * 类型标识
	 * */
	public String name();
}
