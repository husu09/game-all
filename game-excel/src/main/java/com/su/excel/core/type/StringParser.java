package com.su.excel.core.type;

public class StringParser implements TypeParser<String> {

	@Override
	public String parser(String value) {
		return value;
	}

	@Override
	public String name() {
		return "string";
	}

}
