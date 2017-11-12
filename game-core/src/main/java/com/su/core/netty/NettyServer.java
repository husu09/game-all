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

import com.su.core.config.AppConfig;
import com.su.core.proto.ProtoLengthPrepender;
import com.su.core.proto.ProtoDecoder;
import com.su.core.proto.ProtoEncoder;

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
public final class NettyServer {

	@Autowired
	private ProtoDecoder protoDecoder;
	@Autowired
	private ProtoEncoder protoEncoder;
	@Autowired
	private NettyServerHandler nettyServerHandler;
	@Autowired
	private HeartbeatHandler heartbeatHandler;
	
	
	private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;
	
	
    public void start() throws Exception {
    	bossGroup = new NioEventLoopGroup(1);
    	workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(
                    		new IdleStateHandler(0, 0, 120),
                    		heartbeatHandler,
                    		protoEncoder,
                    		new ProtoLengthPrepender(),
                    		protoDecoder,
                    		nettyServerHandler);
                }
             });
            // Bind and start to accept incoming connections.
            b.bind(appConfig.getPort()).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    
    public void stop() {
    	bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
