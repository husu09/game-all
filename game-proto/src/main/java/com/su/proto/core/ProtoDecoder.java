package com.su.proto.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.MessageLite;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

@Sharable
@Component
public class ProtoDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Autowired
	private ProtoContext protoContext;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		int nameLen = readRawVarint32(in);
		byte[] nameData = new byte[nameLen];
		in.readBytes(nameData);
		String messageName = new String(nameData, CharsetUtil.UTF_8);
		if (!protoContext.contains(messageName)) {
			throw new Exception("没有这个协议：" + messageName);
		}
		int dataLen = readRawVarint32(in);
		byte[] data = new byte[dataLen];
		in.readBytes(data);
		MessageLite messageLite = protoContext.get(messageName).getDefaultInstanceForType().getParserForType()
				.parseFrom(data);
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
