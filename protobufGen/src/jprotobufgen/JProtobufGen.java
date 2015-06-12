package jprotobufgen;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import protobufgen.IOUtils;
import xgame.core.util.ClassUtils;

import com.baidu.bjf.remoting.protobuf.ProtobufIDLGenerator;
import com.baidu.bjf.remoting.protobuf.annotation.Msg;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;

public class JProtobufGen {
	private static final Logger log = Logger.getLogger(JProtobufGen.class.getName());
	private static final String msg_src_package_path = "../rpg/src/com/shadowgame/rpg/msg";
	private static final String msg_class_path = "../rpg/bin";
	private static final String proto_file_out_dir = "./proto-out";
	private static final String cs_file_out_dir = "./cs-out";
	private static final String msg_registry_file_out_oir = "../rpg/src";
	private static void gen() throws Exception {
		JavaDocBuilder builder = new JavaDocBuilder();
		builder.addSourceTree(new File(msg_src_package_path));
		Map<String, JavaClass> context = new HashMap<String, JavaClass>();
		for (JavaClass cls : builder.getClasses()) {
			context.put(cls.getFullyQualifiedName(), cls);
		}

		File outDirectory = new File(proto_file_out_dir);
		if(outDirectory.exists())
			IOUtils.deleteFile(outDirectory);
		else
			outDirectory.mkdirs();
		
		Map<Integer, String> msgId2ClassName = new TreeMap<Integer, String>();
		Set<String> nestedMsg = new HashSet<String>();
		Map<String, String> files = new LinkedHashMap<String, String>();
		for (Class<?> cls : ClassUtils.scanDir(msg_class_path)) {
			Msg msg = null;
			if((msg = cls.getAnnotation(Msg.class)) != null) {
				files.put(cls.getName(), ProtobufIDLGenerator.getIDL(cls, context, nestedMsg));
				msgId2ClassName.put(msg.value(), cls.getName());
			}
		}
		for (String msgClassName : nestedMsg)
			files.remove(msgClassName);
		for (Map.Entry<String, String> e : files.entrySet()) {
			String filename = e.getKey().replace(".", File.separator) + ".proto";
			File protoFile = xgame.core.util.IOUtils.writeFile(proto_file_out_dir, filename, e.getValue());
			log.info("find Msg class:[" + e.getKey() + "], write file:[" +  filename + "] to [" + proto_file_out_dir + "] success.");
			int i = e.getKey().lastIndexOf(".");
			Proto2Cs.gen(protoFile.getCanonicalPath(), cs_file_out_dir + File.separator + e.getKey().substring(i+1) + ".cs");
			
		}
		if(!msgId2ClassName.isEmpty()) {
			String filename = "Messages.txt";
			StringBuilder str = new StringBuilder();
			for (Entry<Integer, String> e : msgId2ClassName.entrySet()) {
				str.append(e.getKey()).append("=").append(e.getValue()).append("\n");
			}
			String s = str.toString();
			xgame.core.util.IOUtils.writeFile(proto_file_out_dir, filename, s);
			log.info("write file:[" +  filename + "] to [" + proto_file_out_dir + "] success.");
			xgame.core.util.IOUtils.writeFile(msg_registry_file_out_oir, filename, s);
			log.info("write file:[" +  filename + "] to [" + msg_registry_file_out_oir + "] success.");
			xgame.core.util.IOUtils.writeFile(cs_file_out_dir, filename, s);
		}
	}
	
	public static void main(String[] args) throws Exception {
		gen();
	}
}
