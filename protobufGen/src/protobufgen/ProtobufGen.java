package protobufgen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Logger;

import xgame.core.util.ClassUtils;

import com.baidu.bjf.remoting.protobuf.annotation.Msg;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.Type;

@Ignore
public class ProtobufGen {
	private static final Logger log = Logger.getLogger(ProtobufGen.class.getName());
	private static final String msg_src = "../rpg/src/com/shadowgame/rpg/msg";
	private static final String msg_classes = "../rpg/bin";
	private static final String classpath_resources = "../rpg/src";
	private static final String proto_file_out_dir = "proto-out";
	private static final String cs_file = "cs-out/Msg.cs";
	private static final String protoFileSuffix = ".proto";
	private static final String registry_filename = "Messages.txt";

	public static void main(String[] args) throws Exception {
		File outDirectory = new File(proto_file_out_dir);
		if(outDirectory.exists())
			IOUtils.deleteFile(outDirectory);
		else
			outDirectory.mkdirs();
		
		JavaDocBuilder builder = new JavaDocBuilder();
		builder.addSourceTree(new File(msg_src));
		Map<String, JavaClass> jcs = new HashMap<String, JavaClass>();
		for (JavaClass cls : builder.getClasses()) {
			jcs.put(cls.getFullyQualifiedName(), cls);
		}
		
		Map<Integer, String> msgId2ClassName = new TreeMap<Integer, String>();
		List<String> protoFiles = new ArrayList<>(); 
		for (Class<?> cls : ClassUtils.scanDir(msg_classes)) {
			Msg msg = null;
			if((msg = cls.getAnnotation(Msg.class)) != null) {
				StringBuilder fields = new StringBuilder();
				Set<String> imports = new HashSet<String>();
				int i = 1;
				JavaClass javaClass = jcs.get(cls.getName());
				for (JavaField f : javaClass.getFields()) {
					if (!f.isStatic()) {
						Type type = f.getType();
						fields.append(Utils.getComment(f));
						if ((type.isArray()) || (TypeConverter.isCollection(type)))
							fields.append("\trepeated ");
						else {
							fields.append("\toptional ");
						}
						fields.append(TypeConverter.convert(type, imports)).append(" ").append(f.getName())
							.append("=").append(i++).append(";").append(System.lineSeparator());
					}
				}
				StringBuilder protoFile = new StringBuilder();
				protoFile.append("package ").append(javaClass.getPackageName()).append(";").append(System.lineSeparator());
				
//				if(!defaultPackage)
//					protoFile.append("option java_package=\"")
//					.append(javaClass.getPackageName().replace(".protocol.", ".protobuf.")).append("\";")
//					.append(System.lineSeparator());
				
				if(true) {
					protoFile
					.append("option java_outer_classname=\"")
					.append(javaClass.getName()).append("$$Gen").append("\";")
					.append(System.lineSeparator());
				}
				
				for (String im : imports) {
					protoFile.append("import \"").append(im.replace(".", "/") + protoFileSuffix).append("\"")
						.append(";").append(System.lineSeparator());
				}
				protoFile.append(Utils.getClassComment(javaClass));
				protoFile.append("message ").append(javaClass.getName()).append("{").append(System.lineSeparator());
				protoFile.append(fields);
				protoFile.append("}");

				String filename = cls.getName().replace(".", File.separator) + protoFileSuffix;
				File pFile = xgame.core.util.IOUtils.writeFile(proto_file_out_dir, filename, protoFile.toString());

				msgId2ClassName.put(msg.value(), cls.getName());
				log.info("find Msg class:[" + cls.getName() + "], write file:[" +  filename + "] to [" + proto_file_out_dir + "] success.");
				
				protoFiles.add(pFile.getCanonicalPath());
			}
		}
		if(!msgId2ClassName.isEmpty()) {
			StringBuilder str = new StringBuilder();
			for (Entry<Integer, String> e : msgId2ClassName.entrySet()) {
				str.append(e.getKey()).append("=").append(e.getValue()).append("\n");
			}
			xgame.core.util.IOUtils.writeFile(proto_file_out_dir, registry_filename, str.toString());
			xgame.core.util.IOUtils.writeFile(classpath_resources, registry_filename, str.toString());
			log.info("write file:[" +  registry_filename + "] to [" + classpath_resources + "] success.");
			StringBuilder s = new StringBuilder();
			for (String pf : protoFiles) {
				s.append("-i:").append(pf).append(" ");
			}
			proto_net(s.toString());
		}
		log.info("gen proto file success.");
	}
	
	
	private static void proto_net(String _I) throws Exception {
		String cmd = "./protobuf-net/protogen " + _I + "-o:" + cs_file + " -d./" + proto_file_out_dir;
		System.out.println("cmd:" + cmd);
		ProcessBuilder processBuilder = new ProcessBuilder(cmds(cmd));
		Process process = processBuilder.start();
		String error = new String(readBin(process.getErrorStream()));
		if (error.length() > 0) {
			System.err.println("proto-net gen failure:" + error);
		}
		process.destroy();
	}

	private static String[] cmds(String cmd) {
		StringTokenizer st = new StringTokenizer(cmd);
		int n = st.countTokens();
		String[] result = new String[n];
		for (int i = 0; st.hasMoreTokens(); i++)
			result[i] = st.nextToken();
		return result;
	}

	private static byte[] readBin(InputStream in) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len = -1;
		while ((len = in.read(buf)) != -1)
			out.write(buf, 0, len);
		return out.toByteArray();
	}
}