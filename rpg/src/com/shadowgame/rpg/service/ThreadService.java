package com.shadowgame.rpg.service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import xgame.core.util.Service;
import xgame.core.util.ThreadNameFactory;

import com.shadowgame.rpg.core.AppConfig;

public class ThreadService implements Service {
	/**
	 * 线程池
	 */
	public ThreadPoolExecutor threadPool;

	@Override
	public void start() throws Exception {
		threadPool = new ThreadPoolExecutor(
			AppConfig.THREAD_CORE_POOL_SIZE, 
			AppConfig.THREAD_CORE_POOL_SIZE, 
			10,
			TimeUnit.MINUTES, 
			new LinkedTransferQueue<Runnable>(), 
			new ThreadNameFactory("game business logic"));
		threadPool.allowCoreThreadTimeOut(true);
	}

	@Override
	public void stop() throws Exception {
		threadPool.shutdownNow();
	}

	public void execute(Runnable task) {
		threadPool.execute(task);
	}
	
	public <V> Future<V> submit(Callable<V> task) {
		return threadPool.submit(task);
	}

	public int getActiveCount() {
		return threadPool.getActiveCount();
	}

	public int getPoolSize() {
		return threadPool.getPoolSize();
	}

	public String getStatus() {
		return "poolSize=" + getPoolSize() + " queueSize=" + threadPool.getQueue().size() + " activeSize=" + getActiveCount();
	}
}
