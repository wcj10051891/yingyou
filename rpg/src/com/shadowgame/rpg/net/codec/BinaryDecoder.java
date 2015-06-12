package com.shadowgame.rpg.net.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shadowgame.rpg.core.AppConfig;
import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.service.Services;

public class BinaryDecoder extends FrameDecoder {
	private static final Logger log = LoggerFactory.getLogger(BinaryDecoder.class);
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		/**
		 * 4字节长度			+		bytes		<br/>
		 * 长度=4(len)+4(协议号)+bytes数据长度
		 */
		if(buffer.readableBytes() < AppConfig.packet_length_size)
			return null;
		
		buffer.markReaderIndex();
		int dataBodySize = buffer.readInt();
		dataBodySize -= AppConfig.packet_length_size;
		if(buffer.readableBytes() < dataBodySize) {
			buffer.resetReaderIndex();
			return null;
		}
		int msgId = buffer.readInt();
		dataBodySize -= AppConfig.msgId_size;
		ChannelBuffer data = ChannelBuffers.buffer(dataBodySize);
		buffer.readBytes(data, dataBodySize);
		try {
			Object msg = Services.msgService.decode(msgId, data);
			log.info("decode message from player:{}, channel:{} success, msg:{}", channel.getAttachment(), channel, msg);
			return msg;
		} catch (Exception e) {
			throw new AppException("decode message from player:" + channel.getAttachment() + 
					", channel:" + channel + " error", e);
		}
	}
}