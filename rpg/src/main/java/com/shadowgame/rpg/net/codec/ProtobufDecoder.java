package com.shadowgame.rpg.net.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.modules.protobuf.dto.RequestDtoPROTO.RequestDto;
import com.shadowgame.rpg.util.ProtobufUtils;

public class ProtobufDecoder extends FrameDecoder {
	private static final Logger log = LoggerFactory.getLogger(ProtobufDecoder.class);

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
			RequestDto request = (RequestDto)ProtobufUtils.newBuilder(RequestDto.class.getName()).mergeFrom(data).build();
			log.info("decode message {} -> {}", buffer, request);
			return request;
		} catch (Exception e) {
			throw new AppException("protobuf object decode error.", e);
		}
	}
}