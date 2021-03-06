package com.shadowgame.rpg.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xgame.core.util.Config;

public abstract class AppConfig {
	/**
	 * 是否是开发服务器
	 */
	public static final boolean DEBUG;
	/**
	 * tcp监听端口
	 */
	public static final int TCP_PORT;
	/**
	 * 业务线程数量
	 */
	public static final int THREAD_CORE_POOL_SIZE;
	/**
	 * JDK定时器线程数量
	 */
	public static final int JDK_SCHEDULE_CORE_POOL_SIZE;
	/**
	 * Quartz日历定时器线程数量
	 */
	public static final String QUARTZ_SCHEDULE_CORE_POOL_SIZE;
	/**
	 * http监听端口
	 */
	public static final int HTTP_PORT;
	/**
	 * http默认字符集
	 */
	public static final String HTTP_DEFAULT_CHARSET;
	/**
	 * 请求上下文根
	 */
	public static final String HTTP_CONTEXT_ROOT;
	/**
	 * phprpc服务包名
	 */
	public static final String PHPRPC_BEAN_PACKAGE;
	/**
	 * 服务器标识，单个大学字母标识+序号
	 * 如 S1, S2, Q1, Q2, T1, T2...
	 */
	public static final String SERVER_ID;
	/**
	 * 服务器字符标识
	 * A-Z
	 */
	public static final String SERVER_KEY;
	/**
	 * 服务器序号
	 * 0-4096
	 */
	public static final short SERVER_SEQ;
	/**
	 * JMX监控对象包名
	 */
	public static final String JMX_PACKAGE;
	/**
	 * msg包名
	 */
	public static final String MSG_PACKAGE;
	/**
	 * data包名
	 */
	public static final String DATA_PACKAGE;
	/**
	 * servlet包名
	 */
	public static final String SERVLET_PACKAGE;
	/**
	 * 包长度的字节数
	 */
	public static final int packet_length_size = 4;
	/**
	 * 协议号的字节数
	 */
	public static final int msgId_size = 4;
	
	private static int serverSeqMax = 4095;
	private static final Pattern serverIdPTN = Pattern.compile("([A-Z])(\\d+)");
	public static String configFileName = "config.properties";
	public static Config config;
	
	static {
		config = new Config(configFileName);
		DEBUG = config.getBoolean("debug");
		TCP_PORT = config.getInt("tcp.port");
		THREAD_CORE_POOL_SIZE = config.getInt("thread.core.pool.size");
		JDK_SCHEDULE_CORE_POOL_SIZE = config.getInt("jdk.schedule.core.pool.size", 10);
		QUARTZ_SCHEDULE_CORE_POOL_SIZE = config.getString("quartz.schedule.core.pool.size", "2");
		HTTP_PORT = config.getInt("http.port");
		HTTP_DEFAULT_CHARSET = config.getString("http.defaultCharset", "UTF-8");
		HTTP_CONTEXT_ROOT = config.getString("http.context.root", "server");
		PHPRPC_BEAN_PACKAGE = config.getString("phprpc.package");
		MSG_PACKAGE = config.getString("msg.package");
		DATA_PACKAGE = config.getString("data.package");
		SERVLET_PACKAGE = config.getString("servlet.package");
		SERVER_ID = config.getString("serverId");
		try {
			Matcher matcher = serverIdPTN.matcher(SERVER_ID);
			if(matcher.matches()) {
				SERVER_KEY = matcher.group(1);
				SERVER_SEQ = Short.parseShort(matcher.group(2));
			} else {
				throw new IllegalArgumentException("'serverId' error");
			}
			if(SERVER_SEQ > serverSeqMax)
				throw new IllegalArgumentException("'serverId' error");
		} catch (Exception e) {
			throw new AppException("'serverId' parameter config error. pattern:([A-Z])([0-4095]), eg:Q1, A1, B2, C3", e);
		}
		JMX_PACKAGE = config.getString("jmx.package");
	}
}
