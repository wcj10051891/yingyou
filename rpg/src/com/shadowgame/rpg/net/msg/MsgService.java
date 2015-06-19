//package com.shadowgame.rpg.net.msg;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Properties;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import xgame.core.util.Service;
//
//import com.baidu.bjf.remoting.protobuf.Codec;
//import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
//import com.baidu.bjf.remoting.protobuf.annotation.Msg;
//import com.shadowgame.rpg.msg.ProtoMsg;
//
//public class MsgService implements Service {
//	private static final Logger log = LoggerFactory.getLogger(MsgService.class);
//	private static final String message_registry_file_name = "/Messages.txt";
//	private Map<Integer, Class<?>> id2Msg = new HashMap<Integer, Class<?>>();
//	private Map<Integer, Codec<Object>> id2Codec = new HashMap<Integer, Codec<Object>>();
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public void start() throws Exception {
//		Properties msgRegistry = new Properties();
//		msgRegistry.load(this.getClass().getResourceAsStream(message_registry_file_name));
//		for (Entry<Object, Object> e : msgRegistry.entrySet()) {
//			Integer msgId = Integer.valueOf(e.getKey().toString());
//			Class<?> cls = Class.forName(e.getValue().toString());
//			id2Msg.put(msgId, cls);
//			id2Codec.put(msgId, (Codec<Object>) ProtobufProxy.create(cls));
//			log.info("find msg {}:{}", msgId, cls);
//		}
//	}
//	
//	public byte[] encodeMsg(Object msg) throws Exception {
//		ProtoMsg p = new ProtoMsg();
//		int msgId = msg.getClass().getAnnotation(Msg.class).value();
//		p.id = msgId;
//		p.data = id2Codec.get(msgId).encode(msg);
//		return id2Codec.get(ProtoMsg.class.getAnnotation(Msg.class).value()).encode(p);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public <T> T decodeMsg(byte[] data) throws Exception {
//		ProtoMsg msg = (ProtoMsg)id2Codec.get(ProtoMsg.class.getAnnotation(Msg.class).value()).decode(data);
//		return (T)id2Codec.get(msg.id).decode(msg.data);
//	}
//
//	@Override
//	public void stop() throws Exception {
//	}
//
//}
