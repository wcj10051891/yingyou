package xgame.core.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessQueue {
	private static final Logger log = LoggerFactory.getLogger(ProcessQueue.class);
	private LockFreeDeque<FutureTask<?>> taskQueue;
	private ExecutorService threadPool;
	private volatile Future<?> processing;

	public ProcessQueue(ExecutorService threadPool) {
		this.taskQueue = new LockFreeDeque<FutureTask<?>>();
		this.threadPool = threadPool;
	}
	
	private void checkProcessing() {
		if (processing == null || processing.isDone())
			processing = this.threadPool.submit(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							taskQueue.peek().run();
						} catch (Exception e) {
							log.error("process queue run error.", e);
						}
						if(taskQueue.isEmptyAfterPoll())
							break;
					}
				}
			});
	}
	
	public <T> FutureTask<T> submit(Callable<T> callable) {
		FutureTask<T> futureTask = new FutureTask<T>(callable);
		this.taskQueue.offer(futureTask);
		checkProcessing();
		return futureTask;
	}

	public void execute(Runnable runnable) {
		this.taskQueue.offer(new FutureTask<Object>(runnable, null));
		checkProcessing();
	}
}