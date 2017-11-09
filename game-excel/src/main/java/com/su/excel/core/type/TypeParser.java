package com.su.excel.core.type;

public interface TypeParser<T> {
	public T parser(String value);

	public String name();
}
