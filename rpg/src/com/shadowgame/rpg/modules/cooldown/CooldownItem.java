package com.shadowgame.rpg.modules.cooldown;

import java.util.concurrent.TimeUnit;

/**
 * 冷却数据
 * @author wcj10051891@gmail.com
 * @date 2015年7月10日 下午5:58:54
 */
public class CooldownItem {
	/**
	 * 开始时间
	 */
	private long start;
	/**
	 * 冷却多少秒
	 */
	private int delaySeconds;

	public CooldownItem(long start, int delaySeconds) {
		super();
		this.start = start;
		this.delaySeconds = delaySeconds;
	}
	
	/**
	 * 剩余多少秒
	 * @return
	 */
	public int getRemain() {
		long remain = start + delaySeconds - System.currentTimeMillis();
		if(remain <= 0)
			return 0;
		return (int)TimeUnit.MILLISECONDS.toSeconds(remain);
	}
	
	/**
	 * 是否冷却中
	 * @return
	 */
	public boolean isCooldown() {
		return getRemain() > 0;
	}
}
