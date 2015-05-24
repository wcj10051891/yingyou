package xgame.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class Dynamic {
	public static boolean redifineClass(String sourceCode) throws Exception {
		InstrumentAgent.instrument.redefineClasses(StringSourceCompiler.compile(sourceCode));
		return true;
	}
	
	public static Object run(String sourceCode) throws Exception {
		return invoke(sourceCode, "run");
	}
	
	public static Object newInstance(String sourceCode) throws Exception {
		return StringSourceCompiler.compile(sourceCode).getDefinitionClass().newInstance();
	}
	
	public static Object invoke(String sourceCode, String methodName) throws Exception {
		Object instance = newInstance(sourceCode);
		Method method = instance.getClass().getDeclaredMethod(methodName);
		method.setAccessible(true);
		return method.invoke(instance);
	}
	
	//读取或者设置private或者final的字段
    public static Object hackGet(Object obj, String fieldName) throws Exception
    {
        Field f = obj.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        return f.get(obj);
    }
    
    public static void hackSet(Object obj, String fieldName, Object val) throws Exception
    {
        Field f = obj.getClass().getDeclaredField(fieldName);
        if (Modifier.isFinal(f.getModifiers()))
        {
            Field fm = f.getClass().getDeclaredField("modifiers");
            fm.setAccessible(true);
            int mod = f.getModifiers() & ~Modifier.FINAL;       //00000010        11111101
            fm.setInt(f, mod);
        }
        f.setAccessible(true);
        f.set(obj, val);
    }
}