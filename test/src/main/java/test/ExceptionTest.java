package test;

import java.lang.Thread.UncaughtExceptionHandler;

public class ExceptionTest {
	public static void main(String[] args) {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
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
		t.interrupt();
	}
}
