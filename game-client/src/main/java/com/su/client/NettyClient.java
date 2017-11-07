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
package com.su.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.su.proto.core.ProtoDecoder;
import com.su.proto.core.ProtoEncoder;
import com.su.proto.core.ProtoLengthPrepender;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Modification of {@link EchoClient} which utilizes Java object serialization.
 */
@Component
public final class NettyClient {
	@Autowired
	private ProtoEncoder protoEncoder;
	@Autowired
	private ProtoDecoder protoDecoder;

	private EventLoopGroup group;

	public void start(String host, int port) throws Exception {

		group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(protoEncoder, new ProtoLengthPrepender(), protoDecoder, new NettyClientHandler());
				}
			});

			// Start the connection attempt.
			b.connect(host, port).sync().channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}

	public void stop() {
		if (group != null) {
			group.shutdownGracefully();
		}
	}
}
