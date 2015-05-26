package com.baidu.bjf.remoting.protobuf.utils;

import com.thoughtworks.qdox.model.AbstractJavaEntity;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;

public abstract class Utils {
	private static String line = "\n";
	
	public static String getComment(AbstractJavaEntity javaEntity) {
		StringBuilder comments = new StringBuilder();
		String comment = javaEntity.getComment();
		if (comment != null)
			comments.append("\t * ")
					.append(comment.replaceAll(line,
							line + "\t * "))
					.append(line);
		for (DocletTag docletTag : javaEntity.getTags()) {
			String tagName = docletTag.getName();
			String comm = docletTag.getValue();
			comments.append("\t * @").append(tagName).append(" ").append(comm);
			comments.append(line);
		}
		if (comments.length() > 0) {
			return "\t/**" + line + comments + "\t */"
					+ line;
		}
		return "";
	}
	
	public static String getClassComment(JavaClass javaClass) {
		StringBuilder comments = new StringBuilder();
		String comment = javaClass.getComment();
		if (comment != null)
			comments.append("\t * ")
					.append(comment.replaceAll(line,
							line + "\t * "))
					.append(line);
		for (DocletTag docletTag : javaClass.getTags()) {
			String tagName = docletTag.getName();
			String comm = docletTag.getValue();
			comments.append("\t * @").append(tagName).append(" ").append(comm);
			comments.append(line);
		}
		comments.append("字段列表:").append(line);
		for (JavaField f : javaClass.getFields()) {
			comments.append("//").append(f.getComment()).append(line)
				.append(f.getType().getGenericValue()).append(" ").append(f.getName()).append(line);
		}
		if (comments.length() > 0) {
			return "\t/**" + line + comments + "\t */"
					+ line;
		}
		return "";
	}
}