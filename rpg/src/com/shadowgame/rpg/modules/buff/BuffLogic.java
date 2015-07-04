package com.shadowgame.rpg.modules.buff;

import com.shadowgame.rpg.persist.entity.TBuff;

public interface BuffLogic {
	/**
	 * 获取所属buff类型
	 */
	public abstract TBuff getBuff();
	/**
	 * 初始执行之后间隔多少毫秒执行一次
	 */
	public abstract long getPeriod(BuffTask task);
	/**
	 * buff生效时候执行逻辑
	 */
	public abstract void onStart(BuffTask task);
	/**
	 * buff周期性执行逻辑
	 */
	public abstract void onPeriod(BuffTask task);
	/**
	 * buff失效时候执行逻辑
	 */
	public abstract void onStop(BuffTask task);
	/**
	 * 检测是否要终止执行
	 * @param total			已执行总次数
	 * @param startTime		首次执行时间
	 * @return
	 */
	public abstract boolean isFinish(BuffTask task);
}
