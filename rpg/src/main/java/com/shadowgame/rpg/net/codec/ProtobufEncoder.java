package com.shadowgame.rpg.net.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.net.server.tcp.Encoder;

import com.shadowgame.rpg.util.ProtobufUtils;

public class ProtobufEncoder implements Encoder {
	private static final Logger log = LoggerFactory.getLogger(ProtobufEncoder.class);
	public ChannelBuffer encode(Object message) {
		byte[] data = ProtobufUtils.toResponseDto(message).toByteArray();
		ChannelBuffer packet = ChannelBuffers.buffer(4 + data.length);
		packet.writeInt(data.length);
		packet.writeBytes(data);
		log.info("encode message {} -> {}", message, packet);
		return packet;
	}
}