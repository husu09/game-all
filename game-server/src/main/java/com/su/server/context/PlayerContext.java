package com.su.server.context;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.su.common.po.Player;
import com.su.proto.CommonProto.ErrorResp;
import com.su.server.akka.ProcessorActor;
import com.su.server.netty.NettyServerHandler;

import io.netty.channel.ChannelHandlerContext;

/**
 * 玩家上下文
 */
public class PlayerContext {

	private ChannelHandlerContext ctx;

	private ProcessorActor actor;

	private Player player;
	

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public ProcessorActor getActor() {
		return actor;
	}

	public void setActor(ProcessorActor actor) {
		this.actor = actor;
	}

	public Player getPlayer() {
		return player;
	}

	public void handleLogin(Player player) {
		this.player = player;
		ctx.channel().attr(NettyServerHandler.PLAYER_CONTEXT_KEY).set(this);
	}

	public void write(MessageLiteOrBuilder msg) {
		if (msg instanceof MessageLite) {
			ctx.writeAndFlush(msg);
		}
		if (msg instanceof MessageLite.Builder) {
			ctx.writeAndFlush(((MessageLite.Builder) msg).build());
		}

	}

	/**
	 * 发送错误提示
	 */
	public void sendError(int errCode, Object... parameters) {

		ErrorResp.Builder builder = ErrorResp.newBuilder().setErrorCode(errCode);
		for (Object o : parameters) {
			builder.addParameters(o.toString());
		}
		ctx.writeAndFlush(builder.build());
	}

	public static void sendError(ChannelHandlerContext ctx, int errCode, Object... parameters) {

		ErrorResp.Builder builder = ErrorResp.newBuilder().setErrorCode(errCode);
		for (Object o : parameters) {
			builder.addParameters(o.toString());
		}
		ctx.writeAndFlush(builder.build());
	}

}
