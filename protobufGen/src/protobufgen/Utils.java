package protobufgen;

import com.thoughtworks.qdox.model.AbstractJavaEntity;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;

@Ignore
public abstract class Utils {
	public static String getFullComment(AbstractJavaEntity javaEntity) {
		StringBuilder comments = new StringBuilder();
		String comment = javaEntity.getComment();
		if (comment != null)
			comments.append("\t * ")
					.append(comment.replaceAll(System.lineSeparator(),
							System.lineSeparator() + "\t * "))
					.append(System.lineSeparator());
		for (DocletTag docletTag : javaEntity.getTags()) {
			String tagName = docletTag.getName();
			String comm = docletTag.getValue();
			comments.append("\t * @").append(tagName).append(" ").append(comm);
			comments.append(System.lineSeparator());
		}
		if (comments.length() > 0) {
			return "\t/**" + System.lineSeparator() + comments + "\t */"
					+ System.lineSeparator();
		}
		return "";
	}
	
	public static String getClassComment(JavaClass javaClass) {
		StringBuilder comments = new StringBuilder();
		String comment = javaClass.getComment();
		if (comment != null)
			comments.append("\t * ")
					.append(comment.replaceAll(System.lineSeparator(),
							System.lineSeparator() + "\t * "))
					.append(System.lineSeparator());
		for (DocletTag docletTag : javaClass.getTags()) {
			String tagName = docletTag.getName();
			String comm = docletTag.getValue();
			comments.append("\t * @").append(tagName).append(" ").append(comm);
			comments.append(System.lineSeparator());
		}
		comments.append("字段列表:").append(System.lineSeparator());
		for (JavaField f : javaClass.getFields()) {
			comments.append("//").append(f.getComment()).append(System.lineSeparator())
				.append(f.getType().getGenericValue()).append(" ").append(f.getName()).append(System.lineSeparator());
		}
		if (comments.length() > 0) {
			return "\t/**" + System.lineSeparator() + comments + "\t */"
					+ System.lineSeparator();
		}
		return "";
	}
}