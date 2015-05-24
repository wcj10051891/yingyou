package xgame.core.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;

public abstract class BeanUtils {
	public static Map<Class<?>, MethodAccess> methods = new HashMap<Class<?>, MethodAccess>(0);
	public static Map<Class<?>, FieldAccess> fields = new HashMap<Class<?>, FieldAccess>(0);
	
	public static Map<String, Object> toMap(Object bean) {
		FieldAccess fieldAccess = getFieldAccess(bean.getClass());
		Map<String, Object> result = new HashMap<String, Object>();
		for (String fieldName : fieldAccess.getFieldNames())
			result.put(fieldName, fieldAccess.get(bean, fieldName));
		return result;
	}
	
	public static Field getField(Object bean, String fieldName) {
		try {
			Field field = bean.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			throw new IllegalArgumentException("get bean=" + bean + " fieldName=" + fieldName + " error.", e);
		}
	}
	
	private static FieldAccess getFieldAccess(Class<?> beanClass) {
		FieldAccess fieldAccess = fields.get(beanClass);
		if(fieldAccess == null) {
			fieldAccess = FieldAccess.get(beanClass);
			fields.put(beanClass, fieldAccess);
		}
		return fieldAccess;
	}
	
	private static MethodAccess getMethodAccess(Class<?> beanClass) {
		MethodAccess methodAccess = methods.get(beanClass);
		if(methodAccess == null) {
			methodAccess = MethodAccess.get(beanClass);
			methods.put(beanClass, methodAccess);
		}
		return methodAccess;
	}
	
	public static Object getFieldValue(Object bean, String fieldName) {
		return getFieldAccess(bean.getClass()).get(bean, fieldName);
	}
	
	public static Object invokeMethod(Object bean, String methodName, Object... args) {
		return getMethodAccess(bean.getClass()).invoke(bean, methodName, args);
	}
}
