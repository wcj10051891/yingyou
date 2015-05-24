package xgame.core.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadNameFactory implements ThreadFactory {
	private String subject;
	private AtomicInteger n = new AtomicInteger();
	public ThreadNameFactory(String subject) {
		this.subject = subject;
	}
	public Thread newThread(Runnable r) {
		return new Thread(r, subject + " thread #" + n.incrementAndGet());
	}
}
