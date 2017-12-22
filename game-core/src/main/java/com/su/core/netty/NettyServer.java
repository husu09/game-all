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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.su.proto.core.ProtoDecoder;
import com.su.proto.core.ProtoEncoder;
import com.su.proto.core.ProtoLengthPrepender;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Modification of {@link EchoServer} which utilizes Java object serialization.
 */
@Component
public final class NettyServer implements ApplicationListener<ApplicationContextEvent> {

	@Autowired
	private ProtoDecoder protoDecoder;
	@Autowired
	private ProtoEncoder protoEncoder;
	@Autowired
	private NettyServerHandler nettyServerHandler;
	@Autowired
	private HeartbeatHandler heartbeatHandler;

	@Value("${server.port}")
	private int port;

	private EventLoopGroup bossGroup = null;
	private EventLoopGroup workerGroup = null;

	public void start() {
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline p = ch.pipeline();
							p.addLast(protoEncoder, new ProtoLengthPrepender(), protoDecoder, nettyServerHandler);
							/*
							 * p.addLast(new IdleStateHandler(0, 0, 120),
							 * heartbeatHandler, protoEncoder, new
							 * ProtoLengthPrepender(), protoDecoder,
							 * nettyServerHandler);
							 */
						}
					});
			// Bind and start to accept incoming connections.
			b.bind(port).sync().channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
			;
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public void stop() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					start();
				}
			}, "netty-server").start();
		} else if (event instanceof ContextClosedEvent) {
			stop();
		}

	}
}
