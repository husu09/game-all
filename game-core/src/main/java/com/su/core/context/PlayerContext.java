package com.su.core.context;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.su.core.akka.PlayerActor;
import com.su.core.gambling.GamePlayer;
import com.su.core.netty.NettyServerHandler;
import com.su.msg.CommonMsg.Error_;

import io.netty.channel.ChannelHandlerContext;

/**
 * 玩家上下文
 */
public class PlayerContext {

	private ChannelHandlerContext ctx;

	private PlayerActor actor;
	
	private long playerId;
	
	private GamePlayer gamePlayer;

	public void handleLogin(long playerId) {
		this.playerId = playerId;
		ctx.channel().attr(NettyServerHandler.PLAYER_CONTEXT_KEY).set(this);
	}

	public void write(MessageLiteOrBuilder msg) {
		if (msg == null)
			return;
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

		Error_.Builder builder = Error_.newBuilder().setErrorCode(errCode);
		for (Object o : parameters) {
			builder.addParameters(o.toString());
		}
		ctx.writeAndFlush(builder.build());
	}

	public static void sendError(ChannelHandlerContext ctx, int errCode, Object... parameters) {

		Error_.Builder builder = Error_.newBuilder().setErrorCode(errCode);
		for (Object o : parameters) {
			builder.addParameters(o.toString());
		}
		ctx.writeAndFlush(builder.build());
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public PlayerActor getActor() {
		return actor;
	}

	public void setActor(PlayerActor actor) {
		this.actor = actor;
	}

	public long getPlayerId() {
		return playerId;
	}

	public GamePlayer getGamePlayer() {
		return gamePlayer;
	}

	public void setGamePlayer(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
	}
	
}
