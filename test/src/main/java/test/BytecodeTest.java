package test;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class BytecodeTest {
	public static void main(String[] args) throws Exception {
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.get(A.class.getName());
		CtMethod method = cc.getMethod("print", "()V");
		method.setBody("System.out.println(\"abcd\");");
		Object o = cc.toClass().newInstance();
		System.out.println(o);
//		for (CtMethod m : cc.getMethods()) {
//			System.out.println(m.getName() + "->" + m.getSignature());
//		}
	}
	
	static class A{
		public void print() {
			System.out.println("abc");
		}
	}
}
