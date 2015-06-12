package test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExceptionTest {
	public static void main(String[] args) throws Exception {
//		Thread t = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					throw new RuntimeException(e);
//				}
//			}
//		});
//		t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
//			
//			@Override
//			public void uncaughtException(Thread t, Throwable e) {
//				System.out.println("error:" + e);
//			}
//		});
//		t.start();
//		t.interrupt();
		List<String> l = new ArrayList<String>();
		Field f = A.class.getDeclaredField("l");
		Type subType = ((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0];
		System.out.println(((Class)subType).newInstance());
		
	}
	
	static class A {
		public A(){}
		List<A> l;
	}
}
