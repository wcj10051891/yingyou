//package com.shadowgame.rpg.net.codec;
//
//import org.jboss.netty.buffer.ChannelBuffer;
//import org.jboss.netty.buffer.ChannelBuffers;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import xgame.core.net.server.tcp.Encoder;
//
//import com.baidu.bjf.remoting.protobuf.annotation.Msg;
//import com.shadowgame.rpg.core.AppConfig;
//import com.shadowgame.rpg.core.AppException;
//import com.shadowgame.rpg.service.Services;
//
//public class ProtobufEncoder implements Encoder {
//	private static final Logger log = LoggerFactory.getLogger(ProtobufEncoder.class);
//	
//	public ChannelBuffer encode(Object msg) {
//		/**
//		 * 4字节长度(长度值=4（len）+2（协议号）+数据长度n)+2字节协议号+protobuf bytes
//		 */
//		if((msg.getClass().getAnnotation(Msg.class)) == null)
//			throw new AppException("message must annotated by [" + Msg.class + "]");
//			
//		byte[] data = null;
//		try {
//			data = Services.msgService.encodeMsg(msg);
//		} catch (Exception e) {
//			throw new AppException("encode message error, msg:" + msg, e);
//		}
//		int dataBodySize = AppConfig.packet_length_size + data.length;
//		ChannelBuffer packet = ChannelBuffers.buffer(dataBodySize);
//		packet.writeInt(dataBodySize);
//		packet.writeBytes(data);
//		log.info("encode message success, msg:{}", msg);
//		return packet;
//	}
//}