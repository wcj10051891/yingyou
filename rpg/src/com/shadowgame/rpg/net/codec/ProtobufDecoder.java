package com.shadowgame.rpg.net.codec;

import java.util.Arrays;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shadowgame.rpg.core.AppConfig;
import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.net.msg.Message;
import com.shadowgame.rpg.service.Services;

public class ProtobufDecoder extends FrameDecoder {
	private static final Logger log = LoggerFactory.getLogger(ProtobufDecoder.class);
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		/**
		 * 2字节长度(长度=2字节len+2字节协议号+数据长度)+2字节协议号+protobuf bytes
		 */
		if(buffer.readableBytes() < AppConfig.packet_length_size)
			return null;
		
		buffer.markReaderIndex();
		short dataBodySize = buffer.readShort();
		dataBodySize -= AppConfig.packet_length_size;
		if(buffer.readableBytes() < dataBodySize) {
			buffer.resetReaderIndex();
			return null;
		}
		int msgId = buffer.readShort();
		dataBodySize -= AppConfig.msgId_size;
		byte[] data = new byte[dataBodySize];
		buffer.readBytes(data, 0, data.length);
		try {
			Message msg = Services.msgService.decodeMsg(msgId, data);
			log.info("decode message from player:{}, channel:{} success, msg:{}", channel.getAttachment(), channel, msg);
			return msg;
		} catch (Exception e) {
			throw new AppException("decode message from player:" + channel.getAttachment() + 
					", channel:" + channel + " error, msgId:" + msgId + ", data:" + Arrays.toString(data), e);
		}
	}
}