package com.shadowgame.rpg.modules.skill;

import com.shadowgame.rpg.modules.core.AbstractFighter;
import com.shadowgame.rpg.persist.entity.TSkill;


/**
 * @author wcj10051891@gmail.com
 */
public abstract class AbstractSkill implements SkillLogic {

	protected TSkill entity;
	
	public AbstractSkill(TSkill entity) {
		this.entity = entity;
	}
	
	public void fire(AbstractFighter attacker, int direction) {
		new SkillTask(attacker, direction, this).start();
	}

	@Override
	public long getDelay(SkillTask task) {
		return this.entity.delay;
	}

	@Override
	public long getPeroid(SkillTask task) {
		return this.entity.period;
	}

	@Override
	public void beforeExecute(SkillTask task) {
		System.out.println("before execute skill:" + this.entity.name);
	}

	@Override
	public void execute(SkillTask task) {
		System.out.println("execute skill:" + this.entity.name);
	}

	@Override
	public void afterExecute(SkillTask task) {
		System.out.println("after execute skill:" + this.entity.name);
	}

	@Override
	public boolean isFinish(SkillTask task) {
		if(task.executeCount >= this.entity.count)
			return true;
		return false;
	}
}
