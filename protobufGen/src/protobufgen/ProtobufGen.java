package protobufgen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.Type;

@Ignore
public class ProtobufGen {
	private static final String inputDir = "../rpg/src/main/java/com/shadowgame/rpg/modules/protocol";
	private static final String outDir = "proto-out";
	private static final String protoFileSuffix = "_PROTO.proto";
	private static final Type IGNORE = new Type(Ignore.class.getName());

	public static void main(String[] args) throws Exception {
		File outDirectory = new File(outDir);
		if(outDirectory.exists())
			IOUtils.deleteFile(outDirectory);
		else
			outDirectory.mkdirs();
		
		JavaDocBuilder builder = new JavaDocBuilder();
		builder.addSourceTree(new File(inputDir));
		for (JavaClass javaClass : builder.getClasses()) {
			Annotation[] annotations = javaClass.getAnnotations();
			boolean ignore = false;
			for (Annotation annotation : annotations) {
				if (annotation.getType().isA(IGNORE)) {
					ignore = true;
					break;
				}
			}
			if (!ignore) {
				String classFQN = javaClass.getFullyQualifiedName();
				if (classFQN.endsWith("Ctrl")) {
					for (JavaMethod method : javaClass.getMethods()) {
						if ((method.isPublic()) && (!method.isAbstract()) && (!method.isConstructor())
								&& (!method.isNative()))
							method.isStatic();
					}
				} else {
					StringBuilder fields = new StringBuilder();
					Set<String> imports = new HashSet<String>();
					int i = 1;
					for (JavaField f : javaClass.getFields()) {
						if ((f.isPublic()) && (!f.isStatic())) {
							Type type = f.getType();
							fields.append(Utils.getFullComment(f));
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
					protoFile.append("package ").append(javaClass.getPackageName()).append(";")
					.append(System.lineSeparator());
					
//					if(!defaultPackage)
						protoFile.append("option java_package=\"")
						.append(javaClass.getPackageName().replace(".protocol.", ".protobuf.")).append("\";")
						.append(System.lineSeparator());
					
					for (String im : imports) {
						protoFile.append("import \"").append(im.replace(".", "/") + protoFileSuffix).append("\"")
							.append(";").append(System.lineSeparator());
					}
					protoFile.append(Utils.getClassComment(javaClass));
					protoFile.append("message ").append(javaClass.getName()).append("{").append(System.lineSeparator());
					protoFile.append(fields);
					protoFile.append("}");

					File outFile = new File(outDirectory, classFQN.replace(".", File.separator) + protoFileSuffix);
					if (!outFile.exists()) {
						outFile.getParentFile().mkdirs();
						outFile.createNewFile();
					}
					BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
					writer.write(protoFile.toString());
					writer.flush();
					writer.close();
				}
			}
		}
		System.out.println("gen proto file success.");
	}
}