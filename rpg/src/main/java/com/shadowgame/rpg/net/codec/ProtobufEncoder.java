package com.shadowgame.rpg.net.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.net.protocol.Msg;
import xgame.core.net.server.tcp.Encoder;

import com.shadowgame.rpg.core.AppConfig;
import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.net.msg.Message;
import com.shadowgame.rpg.service.Services;

public class ProtobufEncoder implements Encoder {
	private static final Logger log = LoggerFactory.getLogger(ProtobufEncoder.class);
	public ChannelBuffer encode(Object message) {
		/**
		 * 2字节长度+4字节协议号+protobuf byte
		 */
		if(!(message instanceof Message))
			throw new AppException("message must extends [" + Message.class.getName() + "]");
		Message msg = (Message)message;
		Msg anno = null;
		if((anno = msg.getClass().getAnnotation(Msg.class)) == null)
			throw new AppException("message must annotated by [" + Msg.class + "]");
			
		int msgId = anno.value();
		byte[] data = null;
		try {
//			data = Services.msgService.encodeMsg(msgId, msg);
		} catch (Exception e) {
			throw new AppException("encode message error, msgId:" + msgId + ", msg:" + msg, e);
		}
		int dataBodySize = AppConfig.msgId_size + data.length;
		ChannelBuffer packet = ChannelBuffers.buffer(AppConfig.packet_length_size + dataBodySize);
		packet.writeShort(dataBodySize);
		packet.writeInt(msgId);
		packet.writeBytes(data);
		log.info("encode message success, msgId:{}, msg:{}", msgId, msg);
		return packet;
	}
}