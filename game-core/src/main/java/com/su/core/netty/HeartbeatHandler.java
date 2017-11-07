package com.su.core.netty;

import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

@Sharable
@Component
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {
	
	private static final ByteBuf HEARTBEAT_SEQUENCE =
			Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(
					"HEARTBEAT", CharsetUtil.ISO_8859_1));

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
	
		if (evt instanceof IdleStateEvent) {
			System.out.println("close idle channel");
			ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate())
			.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
	

}
