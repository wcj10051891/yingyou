package xgame.client;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.net.msg.Message;

public class BinaryMsgService {
	private static final Logger log = LoggerFactory.getLogger(BinaryMsgService.class);
	private static final String message_registry_file_name = "/Messages.txt";
	private Map<Integer, Class<?>> id2Msg = new HashMap<Integer, Class<?>>();
	public Map<Class<?>, Integer> msg2Id = new HashMap<Class<?>, Integer>();
	private Map<Class<?>, Field[]> msg2Fields = new HashMap<Class<?>, Field[]>();

	public void start() throws Exception {
		Properties msgRegistry = new Properties();
		msgRegistry.load(this.getClass().getResourceAsStream(message_registry_file_name));
		for (Entry<Object, Object> e : msgRegistry.entrySet()) {
			Integer msgId = Integer.valueOf(e.getKey().toString());
			Class<?> cls = Class.forName(e.getValue().toString());
			id2Msg.put(msgId, cls);
			msg2Id.put(cls, msgId);
			log.info("find msg {}->{}", msgId, cls);
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
	
	public ChannelBuffer encode(Message msg) {
		try {
			ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
			for (Field field : getFields(msg.getClass())) {
				Class<?> type = field.getType();
				if(type == byte.class || type == Byte.class)
					buf.writeByte(field.getByte(msg));
				else if(type == short.class || type == Short.class)
					buf.writeShort(field.getShort(msg));
				else if(type == char.class || type == Character.class)
					buf.writeChar(field.getChar(msg));
				else if(type == int.class || type == Integer.class)
					buf.writeInt(field.getInt(msg));
				else if(type == long.class || type == Long.class)
					buf.writeLong(field.getLong(msg));
				else if(type == float.class || type == Float.class)
					buf.writeFloat(field.getFloat(msg));
				else if(type == double.class || type == Double.class)
					buf.writeDouble(field.getDouble(msg));
				else if(type == String.class) {
					String s = (String)field.get(msg);
					if(s != null) {
						byte[] b = s.getBytes("UTF-8");
						buf.writeShort(b.length);
						buf.writeBytes(b);
					} else {
						buf.writeShort(0);
					}
				} else if(type == List.class) {
					List<?> l = (List<?>)field.get(msg);
					int len = 0;
					ChannelBuffer n = null;
					if(l != null && l.size() > 0) {
						n = ChannelBuffers.dynamicBuffer();
						for (Object o : l) {
							ChannelBuffer nested = encode((Message)o);
							len += nested.readableBytes();
							n.writeBytes(nested);
						}
					}
					buf.writeShort(len);
					if(n != null)
						buf.writeBytes(n);
				}
			}
			return buf;
		} catch (Exception e) {
			throw new AppException("encode msg:" + msg + " error", e);
		}
	}
	
	public Message decode(int msgId, ChannelBuffer buf) {
		try {
			Message cm = (Message)(id2Msg.get(msgId).newInstance());
			for (Field field : getFields(cm.getClass())) {
				Class<?> type = field.getType();
				if(type == byte.class || type == Byte.class)
					field.setByte(cm, buf.readByte());
				else if(type == short.class || type == Short.class)
					field.setShort(cm, buf.readShort());
				else if(type == char.class || type == Character.class)
					field.setChar(cm, buf.readChar());
				else if(type == int.class || type == Integer.class)
					field.setInt(cm, buf.readInt());
				else if(type == long.class || type == Long.class)
					field.setLong(cm, buf.readLong());
				else if(type == float.class || type == Float.class)
					field.setFloat(cm, buf.readFloat());
				else if(type == double.class || type == Double.class)
					field.setDouble(cm, buf.readDouble());
				else if(type == String.class) {
					short len = buf.readShort();
					if(len > 0) {
						byte[] data = new byte[len];
						buf.readBytes(data);
						field.set(cm, new String(data, "UTF-8"));
					}
				} else if(type == List.class) {
					short len = buf.readShort();
					if(len > 0) {
						ChannelBuffer data = ChannelBuffers.buffer(len);
						buf.readBytes(data);
						Class<?> subType = (Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
						List<Object> l = new ArrayList<Object>();
						while(data.readable()) {
							l.add(decode(this.msg2Id.get(subType), data));
							data.discardReadBytes();
						}
						field.set(cm, l);
					}
				}
			}
			return cm;
		} catch (Exception e) {
			throw new AppException("decode msgId:" + msgId + " error", e);
		}
	}
}
