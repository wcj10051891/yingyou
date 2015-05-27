package jprotobufgen;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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
	private static final String msg_src_package_path = "../rpg/src/main/java/com/shadowgame/rpg/msg";
	private static final String msg_class_path = "../rpg/target/classes";
	private static final String proto_file_out_dir = "proto-out";
	private static final String msg_registry_file_out_oir = "../rpg/src/main/resources";
	private static void gen() throws Exception {
		JavaDocBuilder builder = new JavaDocBuilder();
		builder.addSourceTree(new File(msg_src_package_path));
		Map<String, JavaClass> jcs = new HashMap<String, JavaClass>();
		for (JavaClass cls : builder.getClasses()) {
			jcs.put(cls.getFullyQualifiedName(), cls);
		}

		File outDirectory = new File(proto_file_out_dir);
		if(outDirectory.exists())
			IOUtils.deleteFile(outDirectory);
		else
			outDirectory.mkdirs();
		Map<Integer, String> msgId2ClassName = new TreeMap<Integer, String>();
		for (Class<?> cls : ClassUtils.scanDir(msg_class_path)) {
			Msg msg = null;
			if((msg = cls.getAnnotation(Msg.class)) != null) {
				String protoFile = ProtobufIDLGenerator.getIDL(cls, jcs.get(cls.getName()));
				String filename = cls.getName().replace(".", File.separator) + ".proto";
				xgame.core.util.IOUtils.writeFile(proto_file_out_dir, filename, protoFile);
				msgId2ClassName.put(msg.value(), cls.getName());
				log.info("find Msg class:[" + cls.getName() + "], write file:[" +  filename + "] to [" + proto_file_out_dir + "] success.");
			}
		}
		if(!msgId2ClassName.isEmpty()) {
			String filename = "Messages.txt";
			StringBuilder str = new StringBuilder();
			for (Entry<Integer, String> e : msgId2ClassName.entrySet()) {
				str.append(e.getKey()).append("=").append(e.getValue()).append("\n");
			}
			xgame.core.util.IOUtils.writeFile(proto_file_out_dir, filename, str.toString());
			log.info("write file:[" +  filename + "] to [" + proto_file_out_dir + "] success.");
			xgame.core.util.IOUtils.writeFile(msg_registry_file_out_oir, filename, str.toString());
			log.info("write file:[" +  filename + "] to [" + msg_registry_file_out_oir + "] success.");
		}
	}
	
	public static void main(String[] args) throws Exception {
		gen();
	}
}
