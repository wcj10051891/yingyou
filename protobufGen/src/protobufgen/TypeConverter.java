package protobufgen;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.qdox.model.Type;

@Ignore
public abstract class TypeConverter {
	public static Map<String, String> java2proto = new HashMap<String, String>();
	private static Type collectionType = new Type("java.util.Collection");

	static {
		java2proto.put("boolean", "bool");
		java2proto.put("java.lang.Boolean", "bool");

		java2proto.put("int", "int32");
		java2proto.put("java.lang.Integer", "int32");

		java2proto.put("long", "int64");
		java2proto.put("java.lang.Long", "int64");

		java2proto.put("float", "float");
		java2proto.put("java.lang.Float", "float");

		java2proto.put("double", "double");
		java2proto.put("java.lang.Double", "double");

		java2proto.put("java.lang.String", "string");

		java2proto.put("ByteString", "bytes");
		java2proto.put("com.google.protobuf.ByteString", "bytes");
	}

	public static boolean isCollection(Type type) {
		return type.isA(collectionType);
	}

	public static String convert(Type type, Set<String> imports) {
		if (type.isArray() && type.getDimensions() != 1)
			throw new IllegalArgumentException(
					"only support one dimension array.");
		if (isCollection(type)) {
			Type[] actualTypeArguments = type.getActualTypeArguments();
			if ((actualTypeArguments == null)
					|| (actualTypeArguments.length != 1))
				throw new IllegalArgumentException(
						"generic type parameter error.");
		}
		String fqn = type.getFullyQualifiedName();
		String result = (String) java2proto.get(fqn);
		if (result == null) {
			if (isCollection(type)) {
				result = convert(type.getActualTypeArguments()[0], imports);
			} else if (type.isArray()) {
				result = convert(new Type(fqn), imports);
			} else {
				result = type.getGenericValue();
			}
		}
		if (result == null) {
			throw new IllegalArgumentException("type convert error:" + type);
		}
		if ((!java2proto.containsKey(fqn)) && (!fqn.contains("java.")))
			imports.add(fqn);
		return result;
	}
}