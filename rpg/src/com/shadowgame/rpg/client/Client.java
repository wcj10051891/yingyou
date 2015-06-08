package com.shadowgame.rpg.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.Channel;

import xgame.core.util.RandomUtils;

import com.shadowgame.rpg.client.GameClient.Callback;
import com.shadowgame.rpg.msg.LoginMsg;

public class Client {
	private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);
	private static LoginMsg login = new LoginMsg();
	
	public static void main(String[] args) throws Exception {
		GameClient.init();
//		pool.scheduleAtFixedRate(new Runnable() {
//			@Override
//			public void run() {
//				for(int i = 0; i < 100; i++) {
//					try {
//						Channel channel = GameClient.connect();
//						LoginMsg login = new LoginMsg();
//						login.setUsername("12345");
//						login.setPassword("45678");
//						channel.write(login);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}, 0, 1, TimeUnit.SECONDS);
		

//		
//		for(int i = 0; i < 1200; i++) {
//			send();
//		}
		
		Channel channel = GameClient.connect(new Callback() {
			@Override
			public void call(Object msg) {
				System.out.println("received msg:" + msg);
			}
		});
		channel.write(login);
		channel.close();
	}
	
	private static void send() {
		try {
			final Channel channel = GameClient.connect(new Callback() {
				@Override
				public void call(Object msg) {
//					System.out.println("received msg:" + msg);
				}
			});
			pool.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					channel.write(login);
				}
			}, 0, RandomUtils.nextRandomInt(400, 800), TimeUnit.MILLISECONDS);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
