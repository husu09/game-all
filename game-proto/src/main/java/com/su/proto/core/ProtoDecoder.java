package com.su.proto.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.MessageLite;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.util.CharsetUtil;

@Sharable
@Component
public class ProtoDecoder extends ByteToMessageDecoder {
	
	
	private Logger logger = LoggerFactory.getLogger(ProtoDecoder.class);
	
	@Autowired
	private ProtoContext protoContext;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// [总长度|消息名称长度|消息名称数据|数据长度|数据]
		in.markReaderIndex();
		int preIndex = in.readerIndex();
		int length = readRawVarint32(in);
		if (preIndex == in.readerIndex()) {
			return;
		}
		if (length < 0) {
			throw new CorruptedFrameException("negative length: " + length);
		}

		if (in.readableBytes() < length) {
			in.resetReaderIndex();
			return;
		}
		ByteBuf byteBuf = in.readRetainedSlice(length);

		int nameLen = readRawVarint32(byteBuf);
		byte[] nameData = new byte[nameLen];
		byteBuf.readBytes(nameData);
		String messageName = new String(nameData, CharsetUtil.UTF_8);
		if (!protoContext.getMessageLiteMap().containsKey(messageName)) {
			logger.error("not fined message is {}", messageName);
		}
		int dataLen = readRawVarint32(byteBuf);
		byte[] data = new byte[dataLen];
		byteBuf.readBytes(data);
		MessageLite messageLite = protoContext.getMessageLiteMap().get(messageName).getDefaultInstanceForType()
				.getParserForType().parseFrom(data);
		out.add(messageLite);
	}

	/**
	 * Reads variable length 32bit int from buffer
	 *
	 * @return decoded int if buffers readerIndex has been forwarded else
	 *         nonsense value
	 */
	private static int readRawVarint32(ByteBuf buffer) {
		if (!buffer.isReadable()) {
			return 0;
		}
		buffer.markReaderIndex();
		byte tmp = buffer.readByte();
		if (tmp >= 0) {
			return tmp;
		} else {
			int result = tmp & 127;
			if (!buffer.isReadable()) {
				buffer.resetReaderIndex();
				return 0;
			}
			if ((tmp = buffer.readByte()) >= 0) {
				result |= tmp << 7;
			} else {
				result |= (tmp & 127) << 7;
				if (!buffer.isReadable()) {
					buffer.resetReaderIndex();
					return 0;
				}
				if ((tmp = buffer.readByte()) >= 0) {
					result |= tmp << 14;
				} else {
					result |= (tmp & 127) << 14;
					if (!buffer.isReadable()) {
						buffer.resetReaderIndex();
						return 0;
					}
					if ((tmp = buffer.readByte()) >= 0) {
						result |= tmp << 21;
					} else {
						result |= (tmp & 127) << 21;
						if (!buffer.isReadable()) {
							buffer.resetReaderIndex();
							return 0;
						}
						result |= (tmp = buffer.readByte()) << 28;
						if (tmp < 0) {
							throw new CorruptedFrameException("malformed varint.");
						}
					}
				}
			}
			return result;
		}
	}

}
