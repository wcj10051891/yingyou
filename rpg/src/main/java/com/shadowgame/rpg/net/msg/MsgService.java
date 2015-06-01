package com.shadowgame.rpg.net.msg;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.util.Service;
import xgame.core.util.StringUtils;

import com.google.protobuf.DescriptorProtos.FileDescriptorProto;
import com.google.protobuf.DescriptorProtos.FileDescriptorSet;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.DynamicMessage.Builder;
import com.shadowgame.rpg.msg.LoginMsg;
import com.shadowgame.rpg.msg.NoticeMsg;

public class MsgService implements Service {
	private static final Logger log = LoggerFactory.getLogger(MsgService.class);
	private static final String message_registry_file_name = "/Messages.txt";
	private Map<Integer, Class<?>> id2Msg = new HashMap<Integer, Class<?>>();
	private Map<String, Descriptor> descriptors = new HashMap<>();

	@Override
	public void start() throws Exception {
		File rootDir = new File(this.getClass().getResource("/").toURI());
		List<File> files = new ArrayList<>();
		listFiles(files, rootDir);
		Map<String, FileDescriptor> singleFileDescriptors = new HashMap<>();
		FileDescriptor[] emptyDepends = new FileDescriptor[0];
		List<FileDescriptorProto> hasDepends = new ArrayList<>();
		for (File file : files) {
			FileDescriptorSet descriptorSet = FileDescriptorSet.parseFrom(new FileInputStream(file));
			for (FileDescriptorProto fdp : descriptorSet.getFileList()) {
				if(fdp.getDependencyCount() == 0) {
					FileDescriptor fd = FileDescriptor.buildFrom(fdp, emptyDepends);
					for (Descriptor descriptor : fd.getMessageTypes()) {
						this.descriptors.put(descriptor.getName(), descriptor);
					}
					singleFileDescriptors.put(fdp.getName(), fd);
				} else {
					hasDepends.add(fdp);
				}
			}
		}
		for (FileDescriptorProto fdp : hasDepends) {
			List<FileDescriptor> depends = new ArrayList<>();
			for (String depend : fdp.getDependencyList()) {
				depends.add(singleFileDescriptors.get(depend));
			}
			FileDescriptor fd = FileDescriptor.buildFrom(fdp, depends.toArray(emptyDepends));
			for (Descriptor descriptor : fd.getMessageTypes())
				this.descriptors.put(descriptor.getName(), descriptor);
		}
		
		Properties msgRegistry = new Properties();
		msgRegistry.load(this.getClass().getResourceAsStream(message_registry_file_name));
		for (Entry<Object, Object> e : msgRegistry.entrySet()) {
			Integer msgId = Integer.valueOf(e.getKey().toString());
			Class<?> cls = Class.forName(e.getValue().toString());
			id2Msg.put(msgId, cls);
		}
		
		LoginMsg o = new LoginMsg();
		o.setUsername("username");
		o.setPassword("password");
		o.setAlertMsg(null);
		o.setMsgs(Arrays.asList("1"));
		o.setNoticeMsgs(Arrays.asList(new NoticeMsg("n1"), new NoticeMsg("n2")));
		
		byte[] data = encodeMsg(o);
		Message msg = decodeMsg(10000, data);
		System.out.println(StringUtils.toString(msg));
	}
	
	private void listFiles(Collection<File> files, File file) {
		if(file.isDirectory()) {
			for (File f : file.listFiles())
				listFiles(files, f);
		} else if(file.getName().endsWith(".desc")){
			files.add(file);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private Object msgToDyna(Object o) throws Exception {
		if(o == null)
			return null;
		Class<? extends Object> clazz = o.getClass();
		Descriptor desc = this.descriptors.get(clazz.getSimpleName());
		if(desc == null)
			return o;
		Map<String, Object> fieldValues = new HashMap<String, Object>();
		for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			fieldValues.put(field.getName(), field.get(o));
		}
		Builder builder = DynamicMessage.newBuilder(desc);
		for (FieldDescriptor fd : desc.getFields()) {
			String fieldName = fd.getName();
			if(fd.isRepeated()) {
				List l = (List)fieldValues.get(fieldName);
				if(l != null) {
					if(l.isEmpty()) {
						builder.setField(fd, l);
					} else {
						for (int i = 0; i < l.size(); i++) {
							builder.addRepeatedField(fd, msgToDyna(l.get(i)));
						}
					}
				}
			} else {
				Object val = msgToDyna(fieldValues.get(fieldName));
				if(val != null)
					builder.setField(fd, val);
			}
		}
		return builder.build();
	}
	
	private Object dynaToMsg(Object o, Class<?> msgClass) throws Exception {
		if(o == null)
			return null;
		Descriptor desc = this.descriptors.get(msgClass.getSimpleName());
		if(desc == null)
			return o;
		DynamicMessage msg = (DynamicMessage)o;
		Object msgInstance = msgClass.newInstance();
		for (Map.Entry<FieldDescriptor, Object> e : msg.getAllFields().entrySet()) {
			FieldDescriptor fd = e.getKey();
			String fieldName = fd.getName();
			Field declaredField = msgClass.getDeclaredField(fieldName);
			declaredField.setAccessible(true);
			Object value = e.getValue();
			if(fd.isRepeated()) {
				List l = (List)value;
				if(l == null || l.isEmpty()) {
					declaredField.set(msgInstance, l);
				} else {
					List<Object> lv = new ArrayList<>();
					for (Object object : l) {
						lv.add(dynaToMsg(object, declaredField.getType()));
					}
					declaredField.set(msgInstance, lv);
				}
			} else {
				declaredField.set(msgInstance, dynaToMsg(value, declaredField.getType()));
			}
		}
		return msgInstance;
	}

	public byte[] encodeMsg(Message msg) throws Exception {
		return ((DynamicMessage)msgToDyna(msg)).toByteArray();
	}
	
	public Message decodeMsg(int msgId, byte[] data) throws Exception {
		Class<?> clazz = this.id2Msg.get(msgId);
		Descriptor desc = this.descriptors.get(clazz.getSimpleName());
		return (Message)dynaToMsg(DynamicMessage.parseFrom(desc, data), clazz);
	}

	@Override
	public void stop() throws Exception {
	}

}
