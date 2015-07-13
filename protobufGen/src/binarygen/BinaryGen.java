package binarygen;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import xgame.core.util.IOUtils;
import xgame.core.util.StringUtils;

import com.shadowgame.rpg.net.msg.Message;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.Type;

public class BinaryGen {
	private static final Logger log = Logger.getLogger(BinaryGen.class.getName());
	private static final String msg_src_package_path = "../rpg/src/com/shadowgame/rpg/msg";
	private static final String msg_file_out_dir = "./msg-out";
	private static final Type listType = new Type(List.class.getName());
	private static final String messageType = Message.class.getName();
	private static final Map<String, String> typeRegister = new HashMap<String, String>();
	static {
		typeRegister.put("byte", "int8");
		typeRegister.put("short", "int16");
		typeRegister.put("int", "int32");
		typeRegister.put("long", "int64");
		typeRegister.put("float", "float");
		typeRegister.put("double", "double");
		typeRegister.put("String", "string");
		typeRegister.put("Byte", "int8");
		typeRegister.put("Short", "int16");
		typeRegister.put("Integer", "int32");
		typeRegister.put("Long", "int64");
		typeRegister.put("Float", "float");
		typeRegister.put("Double", "double");
	}
	
	private static class MessageEntry implements Comparable<MessageEntry>{
		private String name;
		private int type;
		private String content;
		public MessageEntry(String name, String content) {
			this.name = name;
			if(name.startsWith("Cs"))
				type = 1;
			else if(name.startsWith("Sc"))
				type = 2;
			else
				type = 3;
			this.content = content;
		}

		@Override
		public int compareTo(MessageEntry o) {
			if(type < o.type)
				return -1;
			if(type > o.type)
				return 1;
			
			String[] s1 = name.split("_");
			String[] s2 = o.name.split("_");
			if(s1.length == 2 && s2.length == 2) {
				Integer msgId1 = Integer.valueOf(s1[1]);
				Integer msgId2 = Integer.valueOf(s2[1]);
				return msgId1 - msgId2;
			}
			return 0;
		}
		
		@Override
		public String toString() {
			return this.content;
		}
	}
	
	private static void gen() throws Exception {
		JavaDocBuilder builder = new JavaDocBuilder();
		builder.addSourceTree(new File(msg_src_package_path));
		Map<String, List<MessageEntry>> msgs = new LinkedHashMap<String, List<MessageEntry>>();
		for (JavaClass cls : builder.getClasses()) {
			if(cls.isA(messageType)) {
				StringBuilder msg = new StringBuilder();
				msg.append(comment(cls.getComment(), true)).append("\n");
				msg.append("message ").append(cls.getName()).append("{").append("\n");
				for (JavaField f : cls.getFields()) {
					if(f.getType().isA(listType)) {
						String t = f.getType().getActualTypeArguments()[0].getJavaClass().getName();
						String at = typeRegister.get(t);
						if(at != null)
							t = at;
						msg.append("\t").append("array ").append(t).append(" ").append(f.getName()).append("; ").append(comment(f.getComment(), false));
					} else {
						String t = f.getType().getJavaClass().getName();
						String at = typeRegister.get(t);
						if(at == null)
							at = t;
						msg.append("\t").append(at).append(" ").append(f.getName()).append("; ").append(comment(f.getComment(), false));
					}
					msg.append("\n");
				}
				msg.append("}");
				List<MessageEntry> list= msgs.get(cls.getPackageName());
				if(list == null) {
					list = new ArrayList<BinaryGen.MessageEntry>();
					msgs.put(cls.getPackageName(), list);
				}
				list.add(new MessageEntry(cls.getName(), msg.toString())); 
			}
		}

		if(!msgs.isEmpty()) {
			File outDirectory = new File(msg_file_out_dir);
			if(outDirectory.exists())
				IOUtils.deleteFile(outDirectory);
			else
				outDirectory.mkdirs();
			
			for (Entry<String, List<MessageEntry>> e : msgs.entrySet()) {
				Collections.sort(e.getValue());
				String packageName = e.getKey();
				String filename = packageName.substring(packageName.lastIndexOf(".") + 1);
				String[] s = filename.split("_");
				filename = s[1] + "_" + s[0];
				File file = xgame.core.util.IOUtils.writeFile(msg_file_out_dir + File.separator, filename + ".txt", StringUtils.join(e.getValue(), "\n"));
				log.info("package " + packageName + " msg generate success->" + file.getCanonicalPath());
			}
		}
	}
	
	private static String comment(String comment, boolean isClassComment) {
		StringBuilder sb = new StringBuilder(isClassComment ? "// ======== " : "//");
		if(comment != null) {
			StringTokenizer s = new StringTokenizer(comment, System.lineSeparator());
			while(s.hasMoreTokens()) {
				sb.append(s.nextToken()).append("\t");
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args) throws Exception {
		gen();
	}
}
