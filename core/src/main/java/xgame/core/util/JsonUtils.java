package xgame.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public abstract class JsonUtils extends JSON {
//	private static final SerializeConfig config;
//	static {
//		config = new SerializeConfig();
//		// 使用和json-lib兼容的日期输出格式
//		config.put(java.util.Date.class, new JSONLibDataFormatSerializer());
//		// 使用和json-lib兼容的日期输出格式
//		config.put(java.sql.Date.class, new JSONLibDataFormatSerializer());
//	}

	private static final SerializerFeature[] features = {
		// 输出空置字段
		SerializerFeature.WriteMapNullValue,
		// list字段如果为null，输出为[]，而不是null
		SerializerFeature.WriteNullListAsEmpty,
		// 数值字段如果为null，输出为0，而不是null
		SerializerFeature.WriteNullNumberAsZero,
		// Boolean字段如果为null，输出为false，而不是null
		SerializerFeature.WriteNullBooleanAsFalse,
		// 字符类型字段如果为null，输出为""，而不是null
		SerializerFeature.WriteNullStringAsEmpty
	};

	// 序列化为和JSON-LIB兼容的字符串
	public static String toJson(Object object) {
		return JSON.toJSONString(object, features);
	}
}
