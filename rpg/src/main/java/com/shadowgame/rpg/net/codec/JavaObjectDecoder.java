package com.shadowgame.rpg.net.codec;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shadowgame.rpg.core.AppException;

public class JavaObjectDecoder extends FrameDecoder {
	private static final Logger log = LoggerFactory.getLogger(JavaObjectDecoder.class);

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		if(buffer.readableBytes() < 4)
			return null;
		
		buffer.markReaderIndex();
		int len = buffer.readInt();
		if(buffer.readableBytes() < len) {
			buffer.resetReaderIndex();
			return null;
		}
		byte[] data = new byte[len];
		buffer.readBytes(data, 0, len);
		try {
			Object readObject = new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			log.info("decode message {} -> {}", buffer, readObject);
			return readObject;
		} catch (Exception e) {
			throw new AppException("object decode error.", e);
		}
	}
}
