package xgame.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ObjectUtils {
	private static final Map<Class<?>, Class<?>> typeMap = new HashMap<Class<?>, Class<?>>();
	static{
		typeMap.put(Boolean.TYPE, Boolean.class);
		typeMap.put(Character.TYPE, Character.class);
		typeMap.put(Byte.TYPE, Byte.class);
		typeMap.put(Short.TYPE, Short.class);
		typeMap.put(Integer.TYPE, Integer.class);
		typeMap.put(Long.TYPE, Long.class);
		typeMap.put(Float.TYPE, Float.class);
		typeMap.put(Double.TYPE, Double.class);
		typeMap.put(Void.TYPE, Void.class);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T create(Class clazz, Class[] paramCls, Object[] param){
		try {
			Constructor con = clazz.getConstructor(paramCls);
			return (T)con.newInstance(param);
		} catch (Exception e) {
			throw new IllegalArgumentException("create class [" + clazz + "] instance error", e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T create(Class clazz){
		try {
			Constructor con = clazz.getConstructor();
			return (T)con.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("create class [" + clazz + "] instance error", e);
		}
	}
	
	public static <T> T create(String className){
		return create(forName(className));
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> forName(String className){
		try {
			return (Class<T>)Class.forName(className);
		} catch (Exception e) {
			throw new IllegalArgumentException("load class [" + className + "] error", e);
		}
	}
	
	public static Method findMatchedMethod(Class<?> clz, String methodName, List<Class<?>> paramTypes){
        if(clz == null)
            return null;
        
        Method[] mes = clz.getMethods();
        for(Method me : mes){
            if(methodName.equals(me.getName())){
                Class<?>[] tars = me.getParameterTypes();
                if((paramTypes == null || paramTypes.size() == 0) && tars.length == 0){
                    return me;
                }
                if(tars.length != paramTypes.size()){
                    continue;
                }
                int z = 0;
                for(int i = 0; i < tars.length; i++){
                    Class<?> a = tars[i];
                    Class<?> b = paramTypes.get(i);
                    if(tars[i].isPrimitive()){
                        a = toObjectType(tars[i]);
                    }
                    if(paramTypes.get(i).isPrimitive()){
                        b = toObjectType(paramTypes.get(i));
                    }
                    if(a == b){
                        z++;
                    }else{
                        break;
                    }
                }
                if(z == tars.length){
                    return me;
                }
            }
        }
        return null;
	}
	
	public static Method findMatchedMethod(String className, String methodName, List<Class<?>> paramTypes){
	    return findMatchedMethod(forName(className), methodName, paramTypes);
	}
	
	private static Class<?> toObjectType(Class<?> primitive){
		if(primitive == null)
			return null;
		if(!primitive.isPrimitive())
			return primitive;
		return typeMap.get(primitive);
	}

    public static Object getProperty(Object instance, String fieldName)
    {
        try
        {
            return org.apache.commons.beanutils.PropertyUtils.getProperty(instance, fieldName);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
