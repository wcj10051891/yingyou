package test;

import java.lang.Thread.UncaughtExceptionHandler;

public class ExceptionTest {
	public static void main(String[] args) throws Exception {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!Thread.currentThread().isInterrupted()){
					System.out.println(System.currentTimeMillis());
				}
			}
		});
		t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println("error:" + e);
			}
		});
		t.start();
		System.out.println(t.isAlive());
		System.out.println(t.isInterrupted());
		t.interrupt();
	}
}
