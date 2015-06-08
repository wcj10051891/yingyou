package com.shadowgame.rpg.jmx.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import xgame.core.util.StringUtils;

import com.shadowgame.rpg.jmx.IStatistics;

public class Statistics implements IStatistics {
	public long startTime;
	public AtomicInteger requestCount = new AtomicInteger();
	public AtomicLong responseTimeSum = new AtomicLong();
	public AtomicInteger users = new AtomicInteger();
	
	public Statistics() {
		startTime = System.currentTimeMillis();
	}
	
	public long getDuringMills() {
		return System.currentTimeMillis() - startTime;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		long duringMills = getDuringMills();
		result.append("users:").append(users.intValue()).append("\n");
		result.append("reqs:").append(requestCount).append("\n");
		result.append("cost ").append(duringMills).append(" ms, ").append(StringUtils.formatTime(duringMills)).append("\n");
		result.append("response time:").append(responseTimeSum.longValue() / requestCount.doubleValue()).append(" ms\n");
		result.append("QPS ").append(requestCount.floatValue() / TimeUnit.MILLISECONDS.toSeconds(duringMills)).append("\n");
		return result.toString();
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public AtomicInteger getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(AtomicInteger requestCount) {
		this.requestCount = requestCount;
	}

	public AtomicLong getResponseMsSum() {
		return responseTimeSum;
	}

	public void setResponseMsSum(AtomicLong responseMsSum) {
		this.responseTimeSum = responseMsSum;
	}
}
