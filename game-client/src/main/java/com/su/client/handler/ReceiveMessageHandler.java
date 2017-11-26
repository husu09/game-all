package com.su.client.handler;

import org.springframework.stereotype.Component;

import com.google.protobuf.MessageLite;
import com.su.client.core.ClientContext;

@Component
public class ReceiveMessageHandler {

	

	public void process(MessageLite messageLite) {
		
		ClientContext.getInstance().showMessage(messageLite);
	}

}
