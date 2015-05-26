package jprotobufgen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import protobufgen.IOUtils;
import xgame.core.util.ClassUtils;

import com.baidu.bjf.remoting.protobuf.ProtobufIDLGenerator;
import com.baidu.bjf.remoting.protobuf.annotation.Msg;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;

public class JProtobufGen {

	private static final String msg_src_package_path = "../rpg/src/main/java/com/shadowgame/rpg/modules/protocol";
	private static final String msg_class_path = "../rpg/target/classes";
	private static final String outDir = "proto-out";
	private static void gen() throws Exception {
		JavaDocBuilder builder = new JavaDocBuilder();
		builder.addSourceTree(new File(msg_src_package_path));
		Map<String, JavaClass> jcs = new HashMap<String, JavaClass>();
		for (JavaClass cls : builder.getClasses()) {
			jcs.put(cls.getFullyQualifiedName(), cls);
		}

		File outDirectory = new File(outDir);
		if(outDirectory.exists())
			IOUtils.deleteFile(outDirectory);
		else
			outDirectory.mkdirs();
		Map<Integer, String> msgId2ClassName = new TreeMap<Integer, String>();
		for (Class<?> cls : ClassUtils.scanDir(msg_class_path)) {
			Msg msg = null;
			if((msg = cls.getAnnotation(Msg.class)) != null) {
				String protoFile = ProtobufIDLGenerator.getIDL(cls, jcs.get(cls.getName()));
				
				File outFile = new File(outDirectory, cls.getName().replace(".", File.separator) + ".proto");
				if (!outFile.exists()) {
					outFile.getParentFile().mkdirs();
					outFile.createNewFile();
				}
				BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
				writer.write(protoFile.toString());
				writer.flush();
				writer.close();
				msgId2ClassName.put(msg.value(), cls.getName());
			}
		}
		if(!msgId2ClassName.isEmpty()) {
			StringBuilder str = new StringBuilder();
			for (Entry<Integer, String> e : msgId2ClassName.entrySet()) {
				str.append(e.getKey()).append("   ->   ").append(e.getValue()).append("\n");
			}
			File outFile = new File(outDirectory, "config.txt");
			if (!outFile.exists()) {
				outFile.getParentFile().mkdirs();
				outFile.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
			writer.write(str.toString());
			writer.flush();
			writer.close();
		}
	}
	
	public static void main(String[] args) throws Exception {
		gen();
	}
}
