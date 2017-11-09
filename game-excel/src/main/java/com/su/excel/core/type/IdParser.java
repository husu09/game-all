package com.su.excel.core.type;

public class IdParser implements TypeParser<Integer> {

	@Override
	public Integer parser(String value) {
		return Integer.parseInt(value);
	}

	@Override
	public String name() {
		return "id";
	}

}
