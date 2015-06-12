package com.shadowgame.rpg.net.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.net.server.tcp.Encoder;

import com.shadowgame.rpg.core.AppConfig;
import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.net.msg.Message;
import com.shadowgame.rpg.service.Services;

public class BinaryEncoder implements Encoder {
	private static final Logger log = LoggerFactory.getLogger(BinaryEncoder.class);
	
	public ChannelBuffer encode(Object msg) {
		if(!(msg instanceof Message))
			throw new AppException("message must instanceof " + Message.class.getName());
		/**
		 * 4字节长度			+		bytes		<br/>
		 * 长度=4(len)+4(协议号)+bytes数据长度
		 */
		ChannelBuffer data = null;
		try {
			data = Services.msgService.encode((Message)msg);
		} catch (Exception e) {
			throw new AppException("encode message error, msg:" + msg, e);
		}
		int dataBodySize = AppConfig.packet_length_size + AppConfig.msgId_size + data.readableBytes();
		ChannelBuffer packet = ChannelBuffers.buffer(dataBodySize);
		packet.writeInt(dataBodySize);
		packet.writeInt(Services.msgService.msg2Id.get(msg.getClass()));
		packet.writeBytes(data);
		log.info("encode message success, msg:{}", msg);
		return packet;
	}
}