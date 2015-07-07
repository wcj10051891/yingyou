package com.shadowgame.rpg.modules.skill;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import xgame.core.util.CommonUtils;

import com.shadowgame.rpg.modules.core.AbstractFighter;
import com.shadowgame.rpg.modules.map.Point;
import com.shadowgame.rpg.service.Services;


/**
 * 间隔执行的任务（技能）
 * @author wcj10051891@gmail.com
 * @date 2015年6月30日 下午4:52:29
 */
public class SkillTask {
	/**
	 * 攻击者
	 */
	public AbstractFighter attacker;
	/**
	 * 攻击者施放时的朝向
	 */
	public int direction;
	/**
	 * 施放目标
	 */
	public AbstractFighter target;
	/**
	 * 施放目标点
	 */
	public Point targetPoint;
	/**
	 * 调度执行目标，技能
	 */
	public SkillLogic skillLogic;
	/**
	 * 已作用次数
	 */
	public int executeCount;
	/**
	 * 作用开始时间
	 */
	public long startTime;
	/**
	 * 调度任务
	 */
	private ScheduledFuture<?> task;

	public SkillTask(AbstractFighter attacker, AbstractFighter target, SkillLogic skillLogic) {
		super();
		this.attacker = attacker;
		this.direction = attacker.getPosition().getDirection();
		this.target = target;
		this.targetPoint = target.getPosition().getPoint();
		this.skillLogic = skillLogic;
	}
	
	public void start() {
		skillLogic.beforeExecute(this);
		this.startTime = System.currentTimeMillis();
		long delay = CommonUtils.nullLongToDef(skillLogic.getDelay(this));
		long peroid = CommonUtils.nullLongToDef(skillLogic.getPeroid(this));
		if(delay > 0 && peroid > 0) {
			this.task = Services.timerService.jdkScheduler.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					run0();
				}
			}, delay, peroid, TimeUnit.MILLISECONDS);
		} else if(delay > 0) {
			this.task = Services.timerService.jdkScheduler.schedule(new Runnable() {
				@Override
				public void run() {
					run0();
				}
			}, delay, TimeUnit.MILLISECONDS);
		} else if(peroid > 0) {
			this.task = Services.timerService.jdkScheduler.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					run0();
				}
			}, 0, peroid, TimeUnit.MILLISECONDS);
		} else {
			++executeCount;
			skillLogic.execute(this);
			skillLogic.afterExecute(this);
		}
	}
	
	private void run0() {
		++executeCount;
		skillLogic.execute(this);
		if(skillLogic.isFinish(this)) {
			stop();
			skillLogic.afterExecute(this);
		}
	}
	
	public void stop() {
		if(task != null) {
			task.cancel(true);
			task = null;
		}
	}
}
