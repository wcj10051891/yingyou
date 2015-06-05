package com.shadowgame.rpg.jmx;

import javax.management.MXBean;

@MXBean
public interface IThreadPoolStatistics {
	String getPoolStatus(String s);
}
