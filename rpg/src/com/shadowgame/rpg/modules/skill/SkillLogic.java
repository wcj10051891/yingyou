package com.shadowgame.rpg.modules.skill;

import java.util.List;

import com.shadowgame.rpg.modules.core.AbstractFighter;

/**
 * 可调度执行的逻辑，执行一次，再每隔多久执行一次，每次执行完之后检测是否结束执行，若结束则终止执行
 * @author wcj10051891@gmail.com
 * @date 2015年6月30日 下午4:44:37
 */
public interface SkillLogic {
	/**
	 * 初始延迟多少毫秒执行
	 */
	public abstract long getDelay(SkillTask task);
	/**
	 * 初始执行之后每隔多少毫秒执行一次
	 */
	public abstract long getPeroid(SkillTask task);
	/**
	 * 执行实际逻辑的前置检测
	 */
	public abstract void beforeExecute(SkillTask task);
	/**
	 * 实际执行逻辑
	 */
	public abstract void execute(SkillTask task);
	/**
	 * 执行实际逻辑之后的逻辑
	 */
	public abstract void afterExecute(SkillTask task);
	/**
	 * 检测是否要终止执行
	 * @param total			已执行总次数
	 * @param startTime		首次执行时间
	 * @return
	 */
	public abstract boolean isFinish(SkillTask task);
	
	/**
	 * 筛选技能施放时中招的fighters
	 * @param task
	 * @return
	 */
	public abstract List<AbstractFighter> getTargetFighters(SkillTask task);
}
