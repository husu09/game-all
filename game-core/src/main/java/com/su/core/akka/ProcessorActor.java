package com.su.core.akka;

import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

public interface ProcessorActor {
	
	public void process(ChannelHandlerContext ctx, MessageLite messageLite)  throws Exception ;
	
	/**
	 * 关闭 Actor
	 * */
	public void stop();
	
}
