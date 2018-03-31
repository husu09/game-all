package com.su.core.akka;

import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

public interface ProcessorActor {
	
	/**
	 * 处理消息
	 * */
	public void process(ChannelHandlerContext ctx, MessageLite messageLite);
	
	/**
	 * 关闭 Actor
	 * */
	public void stop();
	
}
