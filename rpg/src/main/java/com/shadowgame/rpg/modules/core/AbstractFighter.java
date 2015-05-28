package com.shadowgame.rpg.modules.core;

import xgame.core.util.ProcessQueue;

import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.modules.skill.AbstractSkill;
import com.shadowgame.rpg.modules.skill.AttackCommand;
import com.shadowgame.rpg.service.Services;

/**
 * 可参战单位
 * @author wcj10051891@gmail.com
 */
public abstract class AbstractFighter extends AbstractSpirit {
	/**
	 * 当前气血
	 */
	public int hp;
	/**
	 * 气血最大值
	 */
	public int maxhp;
	/**
	 * 当前法力
	 */
	public int mp;
	/**
	 * 最大法力
	 */
	public int maxmp;
	/**
	 * 攻击
	 */
	public int atk;
	/**
	 * 防御
	 */
	public int def;
	
	/**
	 * 力量
	 */
	public int power;
	/**
	 * 敏捷
	 */
	public int agile;
	/**
	 * 智力
	 */
	public int intell;
	/**
	 * 耐力
	 */
	public int endurance;
	
	/**
	 * 攻击队列，串行处理
	 */
	public ProcessQueue attackQueue = new ProcessQueue(Services.threadService.threadPool);
	
	public Object onAttack(AbstractFighter source, AbstractSkill skill) {
		try {
			return attackQueue.submit(new AttackCommand(source, skill, this)).get();
		} catch (Exception e) {
			throw new AppException("skill attack error, source:" + source + ", target:" + this + ", skill:" + skill, e);
		}
	}
}
