package com.su.core.proto;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.protobuf.MessageLite;

/**
 *	proto协议存储结构
 */
@Component
public class ProtoContext {

	private Map<String, MessageLite> map = new HashMap<>();

	public void add(String messageName, MessageLite messageLite) {
		map.put(messageName, messageLite);
	}

	public MessageLite get(String messageName) {
		return map.get(messageName);
	}

	public boolean contains(String messageName) {
		return map.containsKey(messageName);
	}
}
