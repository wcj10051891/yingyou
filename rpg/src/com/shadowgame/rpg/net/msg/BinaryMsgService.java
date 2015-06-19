package com.shadowgame.rpg.net.msg;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.util.ClassUtils;
import xgame.core.util.Service;

import com.shadowgame.rpg.core.AppConfig;
import com.shadowgame.rpg.core.AppException;

public class BinaryMsgService implements Service {
	private static final Logger log = LoggerFactory.getLogger(BinaryMsgService.class);
	private Map<Integer, Class<?>> id2msg = new HashMap<Integer, Class<?>>();
	public Map<Class<?>, Integer> msg2Id = new HashMap<Class<?>, Integer>();
	private Map<Class<?>, Field[]> msg2Fields = new HashMap<Class<?>, Field[]>();
	@Override
	public void start() throws Exception {
		for (Class<?> clazz : ClassUtils.scanPackage(AppConfig.MSG_PACKAGE)) {
			if(Message.class.isAssignableFrom(clazz)) {
				int id = clazz.getName().hashCode();
				id2msg.put(id, clazz);
				msg2Id.put(clazz, id);
				log.info("find msg {}->{}", id, clazz);
			}
		}
	}
	
	private Field[] getFields(Class<?> clazz) {
		Field[] fields = msg2Fields.get(clazz);
		if(fields == null) {
			fields = clazz.getDeclaredFields();
			for (Field field : fields)
				field.setAccessible(true);
			msg2Fields.put(clazz, fields);
		}
		return fields;
	}
	
	public ChannelBuffer encode(Message msg) throws Exception {
		return encode(msg.getClass(), msg);
	}
	
	private ChannelBuffer encode(Class<?> msgType, Object msg) throws Exception {
		ChannelBuffer out = ChannelBuffers.dynamicBuffer();
		if(Message.class.isAssignableFrom(msgType)) {
			for (Field field : getFields(msgType))
				out.writeBytes(encode(field.getType(), field.get(msg)));
		}
		else if(msgType == byte.class || msgType == Byte.class)
			out.writeByte((Byte)msg);
		else if(msgType == short.class || msgType == Short.class)
			out.writeShort((Short)msg);
		else if(msgType == int.class || msgType == Integer.class)
			out.writeInt((Integer)msg);
		else if(msgType == long.class || msgType == Long.class)
			out.writeLong((Long)msg);
		else if(msgType == float.class || msgType == Float.class)
			out.writeFloat((Float)msg);
		else if(msgType == double.class || msgType == Double.class)
			out.writeDouble((Double)msg);
		else if(msgType == String.class) {
			String s = (String)msg;
			if(s != null) {
				byte[] b = s.getBytes("UTF-8");
				out.writeShort(b.length);
				out.writeBytes(b);
			} else {
				out.writeShort(0);
			}
		} else if(msgType == List.class) {
			List<?> l = (List<?>)msg;
			int len = 0;
			ChannelBuffer n = null;
			if(l != null && l.size() > 0) {
				n = ChannelBuffers.dynamicBuffer();
				for (Object o : l) {
					ChannelBuffer nested = encode(o.getClass(), o);
					len += nested.readableBytes();
					n.writeBytes(nested);
				}
			}
			out.writeShort(len);
			if(n != null)
				out.writeBytes(n);
		} else {
			throw new AppException("encode type:" + msgType + ", msg:" + msg + " error");
		}
		return out;
	}
	

	@SuppressWarnings("unchecked")
	public Message decode(int msgId, ChannelBuffer buf) throws Exception {
		return decodeMessageType((Class<Message>)id2msg.get(msgId), buf);
	}
	
	private Message decodeMessageType(Class<Message> msgClass, ChannelBuffer buf) throws Exception {
		Object result = msgClass.newInstance();
		for (Field field : getFields(msgClass))
			field.set(result, decodeField(field, buf));
		return (Message)result;
	}
	

	@SuppressWarnings("unchecked")
	private Object decodeNotArrayType(Class<?> type, ChannelBuffer buf) throws Exception {
		if(type == byte.class || type == Byte.class)
			return buf.readByte();
		else if(type == short.class || type == Short.class)
			return buf.readShort();
		else if(type == int.class || type == Integer.class)
			return buf.readInt();
		else if(type == long.class || type == Long.class)
			return buf.readLong();
		else if(type == float.class || type == Float.class)
			return buf.readFloat();
		else if(type == double.class || type == Double.class)
			return buf.readDouble();
		else if(type == String.class) {
			short len = buf.readShort();
			if(len > 0) {
				byte[] data = new byte[len];
				buf.readBytes(data);
				return new String(data, "UTF-8");
			} else {
				return "";
			}
		} else if(Message.class.isAssignableFrom(type)) {
			return decodeMessageType((Class<Message>)type, buf);
		} else {
			throw new AppException("decode error, unsupport type:" + type);
		}
	}

	private List<?> decodeArrayType(Class<?> nestedType, ChannelBuffer buf) throws Exception {
		short len = buf.readShort();
		if(len > 0) {
			ChannelBuffer data = ChannelBuffers.buffer(len);
			buf.readBytes(data);
			List<Object> l = new ArrayList<Object>();
			while(data.readable()) {
				l.add(decodeNotArrayType(nestedType, data));
			}
			return l;
		} else {
			return Collections.emptyList();
		}
	}
	
	private Object decodeField(Field f, ChannelBuffer buf) throws Exception {
		Class<?> type = f.getType();
		if(type == List.class) {
			Class<?> subType = (Class<?>)((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0];
			return decodeArrayType(subType, buf);
		} else {
			return decodeNotArrayType(type, buf);
		}
	}

	@Override
	public void stop() throws Exception {
	}
	
}
