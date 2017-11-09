package com.su.excel.core.type;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class TypeManager {
	private Map<String, TypeParser<?>> typeParserMap = new HashMap<>();

	public void put(String string, TypeParser<?> parser) {
		typeParserMap.put(string, parser);
	}
}
