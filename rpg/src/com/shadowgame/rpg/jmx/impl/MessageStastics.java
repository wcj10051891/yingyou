package com.shadowgame.rpg.jmx.impl;

import java.util.concurrent.TimeUnit;

import xgame.core.net.server.tcp.StatHandler;
import xgame.core.util.StringUtils;

import com.shadowgame.rpg.jmx.IMessageStastics;
import com.shadowgame.rpg.service.Services;

public class MessageStastics implements IMessageStastics {
	
	public String toString() {
		StatHandler statHandler = Services.tcpService.getStatHandler();
		if(statHandler == null)
			return "server stat not open";
		
		int in = statHandler.in.get();
		int out = statHandler.out.get();
		int total = in + out;
		StringBuilder result = new StringBuilder();
		long duringMills = System.currentTimeMillis() - statHandler.startTime;
		result.append("users:").append(statHandler.connect.intValue()).append("\n");
		result.append("in:").append(in).append("\n");
		result.append("out:").append(out).append("\n");
		result.append("total:").append(total).append("\n");
		result.append("cost ").append(duringMills).append(" ms, ").append(StringUtils.formatTime(duringMills)).append("\n");
		long duringSeconds = TimeUnit.MILLISECONDS.toSeconds(duringMills);
		result.append("in QPS ").append(in / duringSeconds).append("\n");
		result.append("out QPS ").append(out / duringSeconds).append("\n");
		result.append("total QPS ").append(total / duringSeconds).append("\n");
		return result.toString();
	}
}
