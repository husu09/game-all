package com.su.core.context;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;

import io.netty.channel.ChannelHandlerContext;

public class PlayerContext {
	
	private ChannelHandlerContext ctx;

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	
	public void write(MessageLiteOrBuilder msg) {
		if (msg instanceof MessageLite) {
			ctx.writeAndFlush(msg);
        }
        if (msg instanceof MessageLite.Builder) {
        	ctx.writeAndFlush(((MessageLite.Builder) msg).build());
        }
		
	}
	
	
	
}
