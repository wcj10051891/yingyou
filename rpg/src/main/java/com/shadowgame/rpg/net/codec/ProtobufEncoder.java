package com.shadowgame.rpg.net.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.net.server.tcp.Encoder;

import com.baidu.bjf.remoting.protobuf.annotation.Msg;
import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.net.msg.Message;
import com.shadowgame.rpg.service.Services;

public class ProtobufEncoder implements Encoder {
	private static final Logger log = LoggerFactory.getLogger(ProtobufEncoder.class);
	public ChannelBuffer encode(Object message) {
		/**
		 * 4字节长度+4字节协议号+protobuf byte
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
			data = Services.msgService.encodeMsg(msgId, msg);
		} catch (Exception e) {
			throw new AppException("encode message error, msgId:" + msgId + ", msg:" + msg, e);
		}
		int dataBodySize = 4 + data.length;
		ChannelBuffer packet = ChannelBuffers.buffer(4 + dataBodySize);
		packet.writeInt(dataBodySize);
		packet.writeInt(msgId);
		packet.writeBytes(data);
		log.info("encode message success, msgId:{}, msg:{}", msgId, msg);
		return packet;
	}
}