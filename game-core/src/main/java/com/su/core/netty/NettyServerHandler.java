/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.su.core.netty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.MessageLite;
import com.su.core.akka.AkkaContext;
import com.su.core.akka.ProcessorActor;
import com.su.core.akka.ProcessorActorImpl;
import com.su.core.context.GameContext;
import com.su.core.context.PlayerContext;
import com.su.core.event.GameEventDispatcher;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;

/**
 * Handles both client-side and server-side handler depending on which
 * constructor was called.
 */
@Sharable
@Component
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
	
	@Autowired
	private AkkaContext akkaContext;
	@Autowired
	private GameContext gameContext;
	@Autowired
	private GameEventDispatcher gameEventDispatcher;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String channelId = ctx.channel().id().asLongText();
		if (akkaContext.containsActor(channelId)) {
			akkaContext.getActor(channelId).process(ctx, (MessageLite) msg);
		} else {
			ProcessorActor processorActor = akkaContext.createActor();
			akkaContext.addActor(channelId, processorActor);
			processorActor.process(ctx, (MessageLite) msg);
		}
	}
	
	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 关闭服务中，拒绝连接
		if (gameContext.isStopping()) {
			ctx.close();
		}
	}



	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Attribute<PlayerContext> attr = ctx.channel().attr(ProcessorActorImpl.PLAYER_CONTEXT_KEY);
		if (attr != null) {
			PlayerContext playerContext = attr.get();
			if (playerContext.getPlayer() != null) {
				// 从所有玩家上下文移除
				gameContext.removePlayerContext(playerContext.getPlayer().getId());
				// 退出事件
				gameEventDispatcher.logout(playerContext);
				// 删除 actor
				String channelId = ctx.channel().id().asLongText();
				if (akkaContext.containsActor(channelId)) {
					akkaContext.getActor(channelId).stop();
					akkaContext.removeActor(channelId);
				}
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
	
}
