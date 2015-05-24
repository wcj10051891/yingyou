package com.shadowgame.rpg;
  
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shadowgame.rpg.core.AppConfig;
import com.shadowgame.rpg.service.Services;

public class GameServer {
	private static final Logger log = LoggerFactory.getLogger(GameServer.class);
	
	public static void main(String[] args) {
		log.info("*************************");
		log.info("server starting...");
		log.info("*************************");
		
		long time = System.currentTimeMillis();
		
		try {
			Services.start();
		} catch (Exception e) {
			log.error("server start error.", e);
			try {
				Services.stop();
				System.exit(0);
			} catch (Exception ex) {
			}
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Services.stop();
					log.info("*************************");
					log.info("server shutdown completed.");
					log.info("*************************");
				} catch (Exception e) {
					log.error("server shutdown error.", e);
				}
			}
		}));

		log.info("*************************");
		log.info("server start ok, use {}ms", (System.currentTimeMillis() - time));
		if(AppConfig.DEBUG) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Scanner sc = new Scanner(System.in);
					while(sc.hasNextLine()) {
						if(sc.next().equalsIgnoreCase("exit")) {
							sc.close();
							System.exit(0);
						}
					}
				}
			}, "debug exit listener").start();
			log.info("debug server, input 'exit' in console can shutdown server.");
		}
		log.info("*************************");
	}

}
