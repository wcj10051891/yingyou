package com.shadowgame.rpg.jmx.impl;

import java.util.concurrent.ThreadPoolExecutor;

import com.shadowgame.rpg.jmx.IThreadPoolStatistics;
import com.shadowgame.rpg.service.Services;

public class ThreadPoolStatisticsMXBean implements IThreadPoolStatistics {

	@Override
	public String getPoolStatus(String a) {
		StringBuilder s = new StringBuilder();
		s.append("one shot:").append(print(Services.threadService.threadPool));
		s.append("schedule:").append(print(Services.timerService.jdkScheduler));
		return s.toString();
	}
	
	private String print(ThreadPoolExecutor pool) {
		StringBuilder s = new StringBuilder();
		s
		.append("getTaskCount:").append(pool.getTaskCount()).append("\n")
		.append("getActiveCount:").append(pool.getActiveCount()).append("\n")
		.append("getCompletedTaskCount:").append(pool.getCompletedTaskCount()).append("\n")
		.append("getCorePoolSize:").append(pool.getCorePoolSize()).append("\n")
		.append("getMaximumPoolSize:").append(pool.getMaximumPoolSize()).append("\n")
		.append("getPoolSize:").append(pool.getPoolSize()).append("\n")
		.append("getLargestPoolSize:").append(pool.getLargestPoolSize()).append("\n")
		.append("queue element:").append(pool.getQueue().peek()).append("\n")
		;
		return s.toString();
	}

}
