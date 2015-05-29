package com.shadowgame.rpg.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.cache.CacheService;
import xgame.core.db.DaoContext;
import xgame.core.db.DaoFactory;
import xgame.core.net.remote.servlet.PhpRpcDispatcher;
import xgame.core.net.server.servlet.ServletServer;
import xgame.core.net.server.tcp.TcpService;

import com.shadowgame.rpg.core.AppConfig;
import com.shadowgame.rpg.net.LogicHandler;
import com.shadowgame.rpg.net.codec.ProtobufDecoder;
import com.shadowgame.rpg.net.codec.ProtobufEncoder;
import com.shadowgame.rpg.net.msg.MsgService;
import com.shadowgame.rpg.remote.servlet.Online;

public class Services {
	private static final Logger log = LoggerFactory.getLogger(Services.class);
	public static ThreadService threadService;
	public static TcpService tcpService;
	public static ServletServer servletServer;
	public static TimerService timerService;
	public static JMXService jmxService;
	public static CacheService cacheService;
	public static AppService appService;
	public static MsgService msgService;
	public static DaoFactory daoFactory;
	public static QueryRunner jdbc;
	
	public static void start() throws Exception {
		timerService = new TimerService();
		timerService.start();
		log.info("[timerService] start ok.");
		
		threadService = new ThreadService();
		threadService.start();
		log.info("[theadService] start ok.");
		
		DaoContext daoContext = new DaoContext();
		daoFactory = daoContext.daoFactory;
		log.info("[daoFactory] start ok.");
		
		jdbc = daoContext.jdbc;
		log.info("[jdbc] start ok.");
		
		cacheService = new CacheService(timerService.jdkScheduler);
		cacheService.start();
		log.info("[cacheService] start ok.");
		
		appService = new AppService();
		appService.start();
		log.info("[appService] start ok.");

		msgService = new MsgService();
		msgService.start();
		log.info("[msgService] start ok.");
		
		jmxService = new JMXService();
		jmxService.start();
		log.info("[jmxService] start ok.");
		
		tcpService = new TcpService(AppConfig.TCP_PORT, ProtobufDecoder.class, new LogicHandler(), new ProtobufEncoder());
		tcpService.start();
		log.info("[tcpService] start ok.");
		
		Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>(2);
		//registry servlets
		servlets.put("/online", Online.class);
		servlets.put("/rpc/*", PhpRpcDispatcher.class);
		
		Map<String, String> servletContextAttribute = new HashMap<String, String>();
		servletContextAttribute.put("phprpcBeanPackage", AppConfig.PHPRPC_BEAN_PACKAGE);
		
		servletServer = new ServletServer(AppConfig.HTTP_PORT, AppConfig.HTTP_DEFAULT_CHARSET, AppConfig.HTTP_CONTEXT_ROOT, servletContextAttribute, servlets);
		servletServer.start();
		log.info("[servletServer] start ok.");
	}
	
	public static void stop() throws Exception {
		if(servletServer != null) {
			servletServer.stop();
			log.info("[servletServer] stop ok.");
		}
		
		if(tcpService != null) {
			tcpService.stop();
			log.info("[tcpService] stop ok.");
		}

		if(appService != null) {
			appService.stop();
			log.info("[appService] stop ok.");
		}

		if(cacheService != null) {
			cacheService.stop();
			log.info("[cacheService] stop ok.");
		}

		if(threadService != null) {
			threadService.stop();
			log.info("[threadService] stop ok.");
		}
		
		if(timerService != null) {
			timerService.stop();
			log.info("[timerService] stop ok.");
		}
	}
}
