package com.su.core.akka;

import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

public interface ProcessorActor {
	public void process(ChannelHandlerContext ctx, MessageLite messageLite);
	
	/**
	 * 完成调用后关闭 Actor
	 * */
	public void poisonPill();
	
	/**
	 * 关闭 Actor
	 * */
	public void stop();
	
}
