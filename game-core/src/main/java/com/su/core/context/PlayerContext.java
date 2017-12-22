package com.su.core.context;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.su.proto.CommonProto.ErrorResp;

import io.netty.channel.ChannelHandlerContext;

/**
 * 玩家上下文
 * */
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
	
	public void sendError(String errorCode, String message, Object... parameters) {
		
		ErrorResp.Builder builder = ErrorResp.newBuilder()
		.setErrorCode(errorCode)
		.setErrorMsg(message);
		for (Object o : parameters) {
			builder.addParameters(o.toString());
		}
		ctx.writeAndFlush(builder.build());
	}
	
	
	
}
