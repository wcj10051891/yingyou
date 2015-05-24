package com.shadowgame.rpg.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import xgame.core.util.ClassUtils;
import xgame.core.util.StopWatch;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.JavaType;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import com.shadowgame.rpg.core.AppConfig;
import com.shadowgame.rpg.modules.protobuf.dto.ProtobufDtoPROTO.ProtobufDto;
import com.shadowgame.rpg.modules.protobuf.dto.ResponseDtoPROTO.ResponseDto;
import com.shadowgame.rpg.modules.protocol.dto.chat.ChatDto;
import com.shadowgame.rpg.modules.protocol.dto.chat.ChatListDto;

@SuppressWarnings("unchecked")
public abstract class ProtobufUtils {
	
	private static final String protocol_name = ".protocol.";
	private static final String protobuf_name = ".protobuf.";
	private static final String protofile_suffix = "PROTO";
	
	private static Map<Integer, Class<? extends Message>> id2MessageClass = new HashMap<Integer, Class<? extends Message>>();
	private static Map<Class<? extends Message>, Integer> messageClass2Id = new HashMap<Class<? extends Message>, Integer>();
	private static Map<Class<?>, Class<? extends Message>> javabean2Message = new HashMap<Class<?>, Class<? extends Message>>();
	/**
	 * key：具体protobuf Message类名.字段名<br/>
	 * value：message对应的builder类上设置值的方法
	 */
	private static Map<String, MethodIndex> builderMethods = new HashMap<String, MethodIndex>();
	
	static class MethodIndex {
		public int methodIndex;
		public MethodAccess methodAccess;
		public MethodIndex(int methodIndex, MethodAccess methodAccess) {
			this.methodIndex = methodIndex;
			this.methodAccess = methodAccess;
		}
	}

	static {
		for (Class<?> clazz : ClassUtils.scanPackage(AppConfig.PROTOBUF_PACKAGE_NAME)) {
			if (Message.class.isAssignableFrom(clazz)) {
				int id = clazz.getName().hashCode();
				id2MessageClass.put(id, (Class<? extends Message>) clazz);
				messageClass2Id.put((Class<? extends Message>) clazz, id);
				javabean2Message.put(ProtobufUtils.protobeanToJavabean(clazz), (Class<? extends Message>) clazz);
				Builder builder = null;
				try {
					builder = (Builder) clazz.getDeclaredMethod("newBuilder").invoke(null);
				} catch (Exception e) {
					throw new IllegalArgumentException("message builder not exists.", e);
				}
				Class<?> builderClass = builder.getClass();
				Descriptor descriptor = builder.getDescriptorForType();
				try {
					for (FieldDescriptor field : descriptor.getFields()) {
						if (field.isRepeated()) {
							//获取repeated类型（列表）的元素实际类型
							Class<?> paramType = getType(field.getType().getJavaType(),
									clazz.getMethod(getMethodName("get",field.getName()), int.class).getReturnType());
							MethodAccess methodAccess = MethodAccess.get(builderClass);
							//builder上的add方法添加元素
							builderMethods.put(clazz.getName() + "." + field.getName(), 
								new MethodIndex(methodAccess.getIndex(getMethodName("add", field.getName()), paramType), 
									methodAccess));
						} else {
							//获取实际类型
							Class<?> paramType = getType(field.getType().getJavaType(), 
									clazz.getMethod(getMethodName("get",field.getName())).getReturnType());
							MethodAccess methodAccess = MethodAccess.get(builderClass);
							//builder上的set方法赋值
							builderMethods.put(clazz.getName() + "." + field.getName(), 
								new MethodIndex(methodAccess.getIndex(getMethodName("set", field.getName()), paramType), 
									methodAccess));
						}
					}
				} catch (Exception e) {
					throw new RuntimeException("protobuf registry failure, builder method error.", e);
				}
			}
		}
	}

	private static String getMethodName(String prefix, String fieldName) {
		return prefix + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	}

	private static Class<?> getType(JavaType javaType, Class<?> realType) {
		if (javaType == JavaType.INT)
			return int.class;
		else if (javaType == JavaType.LONG)
			return long.class;
		else if (javaType == JavaType.FLOAT)
			return float.class;
		else if (javaType == JavaType.DOUBLE)
			return double.class;
		else if (javaType == JavaType.BOOLEAN)
			return boolean.class;
		return realType;
	}
	
	private static Map<Class<?>, FastAccess> accesses = new HashMap<Class<?>, FastAccess>();
	private static class FastAccess {
		public Map<Field, Integer> field2Index;
		public FieldAccess fieldAccess;
		public FastAccess(Map<Field, Integer> field2Index, FieldAccess fieldAccess) {
			this.field2Index = field2Index;
			this.fieldAccess = fieldAccess;
		}
	}
	private static FastAccess getFastAccess(Class<?> beanClass) {
		FastAccess access = accesses.get(beanClass);
		if(access == null) {
			Field[] fields = beanClass.getDeclaredFields();
			FieldAccess fieldAccess = FieldAccess.get(beanClass);
			Map<Field, Integer> field2Index = new HashMap<>(fields.length);
			for (Field field : fields)
				field2Index.put(field, fieldAccess.getIndex(field.getName()));
			access = new FastAccess(field2Index, FieldAccess.get(beanClass));
			accesses.put(beanClass, access);
		}
		return access;
	}
	public static Object beanToMessage(Object javaBean) {
		if (javaBean == null)
			return null;
		Class<?> beanClass = javaBean.getClass();
		Class<? extends Message> messageClass = javabean2Message.get(beanClass);
		if(messageClass == null)
			return javaBean;
		String messageTypeName = messageClass.getName();
		Builder builder = null;
		try {
			builder = ProtobufUtils.newBuilder(messageTypeName);
		} catch (Exception e) {
			throw new IllegalArgumentException("param javaBean:" + javaBean + " to protobuf message error.", e);
		}
		FastAccess fastAccess = getFastAccess(beanClass);
		for (Entry<Field, Integer> entry : fastAccess.field2Index.entrySet()) {
			Object fieldValue = fastAccess.fieldAccess.get(javaBean, entry.getValue());
			if (fieldValue == null)
				continue;
			Field beanField = entry.getKey();
			String key = messageTypeName + "." + beanField.getName();
			MethodIndex setValueMethod = builderMethods.get(key);
			Class<?> fieldType = beanField.getType();
			if (javabean2Message.containsKey(fieldType) || Message.class.isAssignableFrom(fieldType)) {
				setValueMethod.methodAccess.invoke(builder, setValueMethod.methodIndex, beanToMessage(fieldValue));
			} else if (Iterable.class.isAssignableFrom(fieldType)) {
				for (Iterator<?> it = ((Iterable<?>) fieldValue).iterator(); it.hasNext();)
					setValueMethod.methodAccess.invoke(builder, setValueMethod.methodIndex, beanToMessage(it.next()));
			} else if (fieldType.isArray()) {
				int length = Array.getLength(fieldValue);
				for (int i = 0; i < length; i++)
					setValueMethod.methodAccess.invoke(builder, setValueMethod.methodIndex, beanToMessage(Array.get(fieldValue, i)));
			} else {
				setValueMethod.methodAccess.invoke(builder, setValueMethod.methodIndex, fieldValue);
			}
		}
		return builder.build();
	}
//
//	public static Object beanToMessageUseReflect(Object javaBean) {
//		if (javaBean == null)
//			return null;
//		StopWatch w = new StopWatch();
//		w.start("getclass");
//		Class<?> beanClass = javaBean.getClass();
//		w.stop();
//		w.start("javabean2Message.containsKey");
//		if(!javabean2Message.containsKey(beanClass))
//			return javaBean;
//		w.stop();
//		w.start("javabean2Message.get");
//		String typeName = javabean2Message.get(beanClass).getName();
//		w.stop();
//		w.start("newBuilder");
//		Builder builder = null;
//		try {
//			builder = ProtobufUtils.newBuilder(typeName);
//		} catch (Exception e) {
//			return javaBean;
//		}
//		w.stop();
//		try {
//			w.start("getDeclaredFields");
//			Field[] fields = beanClass.getDeclaredFields();
//			w.stop();
//			for (Field field : fields) {
//				w.start("field.setAccessible");
//				field.setAccessible(true);
//				w.stop();
//				w.start("field.get");
//				Object value = field.get(javaBean);
//				w.stop();
//				if (value == null)
//					continue;
//				w.start("field.getType");
//				Class<?> fieldType = field.getType();
//				w.stop();
//				w.start("builderMethods.get");
//				//method cache
//				Method method = methods.get(typeName + "." + field.getName());
//				w.stop();
//				w.start("if:" + field.getName());
//				if (Message.class.isAssignableFrom(fieldType) || javabean2Message.get(fieldType) != null) {
//					method.invoke(builder, beanToMessageUseReflect(value));
//				} else if (Iterable.class.isAssignableFrom(fieldType)) {
//					for (Iterator<?> it = ((Iterable<?>) value).iterator(); it.hasNext();)
//						method.invoke(builder, beanToMessageUseReflect(it.next()));
//				} else if (fieldType.isArray()) {
//					int length = Array.getLength(value);
//					for (int i = 0; i < length; i++)
//						method.invoke(builder, beanToMessageUseReflect(Array.get(value, i)));
//				} else {
//					method.invoke(builder, value);
//				}
//				w.stop();
//			}
//			w.start("build");
//			Message msg = builder.build();
//			w.stop();
//			System.out.println("beanToMessageUseReflect");
//			System.out.println(w.prettyPrint());
//			return msg;
//		} catch (Exception e) {
//			throw new RuntimeException("javaBean " + javaBean + " transform failure.", e);
//		}
//	}
	
	public static ResponseDto toResponseDto(Object object) {
		return toResponseDto(0, false, object);
	}

	public static ResponseDto toResponseDto(int sn, boolean isError, Object object) {
		if(object instanceof ResponseDto)
			return (ResponseDto)object;
		
		if(object instanceof Message)
			return responseProto(sn, isError, (Message)object);
		
		return responseJava(sn, isError, object);
	}
	
	private static ResponseDto responseProto(int sn, boolean isError, Message protobufObject) {
		return ResponseDto.newBuilder().setSn(sn).setIsError(isError).setResult(protobuf(protobufObject)).build();
	}
	
	private static ResponseDto responseJava(int sn, boolean isError, Object javaBean) {
		Object protobufObject = beanToMessage(javaBean);
		if(!(protobufObject instanceof Message))
			throw new IllegalArgumentException("param '" + javaBean +"' don't have matched protobuf message.");
		if(protobufObject instanceof ResponseDto)
			return (ResponseDto)protobufObject;
		return ResponseDto.newBuilder().setSn(sn).setIsError(isError).setResult(protobuf(protobufObject)).build();
	}
	
	public static Object getResponseResult(ResponseDto response) throws Exception {
		return decode(response.getResult());
	}
	
	private static ProtobufDto protobuf(Object protobufObject) {
		return ProtobufDto.newBuilder()
			.setTypeId(messageClass2Id.get(protobufObject.getClass()))
			.setData(encode(protobufObject))
			.build();
	}
	
	private static ByteString encode(Object protobufObject) {
		return ((Message)protobufObject).toByteString();
	}
	
	private static Object decode(ProtobufDto protobufDto) throws Exception {
		return decode(protobufDto.getTypeId(), protobufDto.getData().toByteArray());
	}
	
	public static Object decode(Integer typeId, byte[] data) throws Exception {
		return newBuilder(id2MessageClass.get(typeId).getName()).mergeFrom(data).build();
	}
	
	public static Builder newBuilder(String typeName) throws Exception {
		return (Message.Builder)Class.forName(typeName).getDeclaredMethod("newBuilder").invoke(null);
	}
	
	private static <T extends Message> Class<T> javabeanToProtobean(Class<?> beanClass) {
		try {
			return (Class<T>)Class.forName(
				beanClass.getName().replace(protocol_name, protobuf_name) + protofile_suffix + "$" + beanClass.getSimpleName());
		} catch (Exception e) {
			throw new IllegalArgumentException("The proto bean of " + beanClass + " not found.", e);
		}
	}
	
	public static Class<?> protobeanToJavabean(Class<?> protobeanClass) {
		try {
			return Class.forName(protobeanClass.getPackage().getName().replace(protobuf_name, protocol_name) + "." + protobeanClass.getSimpleName());
		} catch (Exception e) {
			throw new IllegalArgumentException("The java bean of " + protobeanClass + " not found.", e);
		}
	}
	
	public static void main(String[] args) {
		ChatListDto dtos = new ChatListDto();
		dtos.chats = new ArrayList<ChatDto>();
		ChatDto c1 = new ChatDto();
		c1.content = "c1";
		c1.sayerId = 1l;
		c1.sayerNickname = "ss";
		
		ChatDto c2 = new ChatDto();
		c2.content = "c2";
		c2.sayerId = 2l;
		c2.sayerNickname = "ss2";
		
		dtos.chats.add(c1);
		dtos.chats.add(c2);
		
		dtos.chats2 = new ArrayList<Integer>();
		dtos.chats2.add(1);
		dtos.chats2.add(1);
		dtos.chats2.add(1);
		StopWatch w = new StopWatch();
		w.start();
//		for(int i = 0; i < 100000; i++)
			Object beanToMessage = beanToMessage(dtos);
		System.out.println(beanToMessage);
		w.stop();
		System.out.println(w.prettyPrint());
	}
}
