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
import com.su.proto.LoginProto.LoginResp;
import com.su.proto.LoginProto.RegisterResp;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handles both client-side and server-side handler depending on which
 * constructor was called.
 */
@Sharable
@Component
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
	
	@Autowired
	private AkkaContext akkaContext;
	
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
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
	
}
