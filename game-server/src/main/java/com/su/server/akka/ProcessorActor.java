package com.su.server.akka;

import com.google.protobuf.MessageLite;
import com.su.server.context.PlayerContext;

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
	
	/**
	 * 退出
	 * */
	public void logout(PlayerContext playerContext);
	
	/**
	 * 检测刷新
	 * */
	public void checkRefresh(PlayerContext playerContext);
	
}
