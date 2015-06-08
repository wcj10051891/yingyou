package com.shadowgame.rpg.net.codec;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.net.server.tcp.Encoder;

import com.shadowgame.rpg.core.AppException;

public class JavaObjectEncoder implements Encoder {
	private static final Logger log = LoggerFactory.getLogger(JavaObjectEncoder.class);
	public ChannelBuffer encode(Object message) {
		try {
			ByteArrayOutputStream t = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(t);
			out.writeObject(message);
			byte[] data = t.toByteArray();
			ChannelBuffer packet = ChannelBuffers.buffer(4 + data.length);
			packet.writeInt(data.length);
			packet.writeBytes(data);
			log.info("encode message {} -> {}", message, packet);
			return packet;
		} catch (Exception e) {
			throw new AppException("object encode error.", e);
		}
	}
}