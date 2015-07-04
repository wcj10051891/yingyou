package com.shadowgame.rpg.service;

import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.cache.CacheService;
import xgame.core.db.DaoContext;
import xgame.core.db.DaoFactory;
import xgame.core.net.server.tcp.TcpService;

import com.shadowgame.rpg.core.AppConfig;
import com.shadowgame.rpg.net.LogicHandler;
import com.shadowgame.rpg.net.codec.BinaryDecoder;
import com.shadowgame.rpg.net.codec.BinaryEncoder;
import com.shadowgame.rpg.net.msg.BinaryMsgService;

public class Services {
	private static final Logger log = LoggerFactory.getLogger(Services.class);
	public static ThreadService threadService;
	public static TcpService tcpService;
	public static TimerService timerService;
	public static JMXService jmxService;
	public static CacheService cacheService;
	public static BinaryMsgService msgService;
	public static DaoFactory daoFactory;
	public static QueryRunner jdbc;
	public static DataService data;
	public static AppService app;
	public static ServletService servletService;
	
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

		msgService = new BinaryMsgService();
		msgService.start();
		log.info("[msgService] start ok.");
		
		jmxService = new JMXService();
		jmxService.start();
		log.info("[jmxService] start ok.");
		
		tcpService = new TcpService(AppConfig.TCP_PORT, BinaryDecoder.class, new LogicHandler(), new BinaryEncoder());
		tcpService.start();
		log.info("[tcpService] start ok.");
		
		servletService = new ServletService();
		servletService.start();
		log.info("[servletService] start ok.");
		
		data = new DataService();
		data.start();
		log.info("[DataService] start ok.");
		
		app = new AppService();
		app.start();
		log.info("[appService] start ok.");
	}
	
	public static void stop() throws Exception {
		if(servletService != null) {
			servletService.stop();
			log.info("[servletService] stop ok.");
		}
		
		if(tcpService != null) {
			tcpService.stop();
			log.info("[tcpService] stop ok.");
		}

		if(app != null) {
			app.stop();
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
