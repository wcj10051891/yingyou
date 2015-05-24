package com.shadowgame.rpg.jmx.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.util.Dynamic;

import com.shadowgame.rpg.jmx.IRemote;

/**
 * @author wcj10051891@gmail.com
 */
public class RemoteMXBean implements IRemote {
	
	private static final Logger log = LoggerFactory.getLogger(RemoteMXBean.class);

	/**
	 * @param srcWithRunMethod
	 * @return
	 */
	@Override
	public String run(String srcWithRunMethod) {
		try {
			return String.valueOf(Dynamic.run(srcWithRunMethod));
		} catch (Exception e) {
			log.error("run method error.", e);
			return "run error:" + e.getMessage();
		}
	}

	/**
	 * @param src
	 * @return
	 */
	@Override
	public String redefineClass(String src) {
		try {
			return String.valueOf(Dynamic.redifineClass(src));
		} catch (Exception e) {
			log.error("redefineClass error.", e);
			return e.getMessage();
		}
	}
	public static class inner {
		public static void run(){
			System.out.println("inner:" + 789);
		}
	}
}