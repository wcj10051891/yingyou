package com.shadowgame.rpg.modules.skill;

import com.shadowgame.rpg.data.BuffData;
import com.shadowgame.rpg.modules.buff.Buff1;
import com.shadowgame.rpg.persist.entity.TSkill;
import com.shadowgame.rpg.service.Services;

/**
 * @author wcj10051891@gmail.com
 */
public class Skill1 extends AbstractSkill {
	
	public Skill1(TSkill entity) {
		super(entity);
	}

	@Override
	public void execute(SkillTask task) {
		new Buff1(Services.data.get(BuffData.class).buffs.get(1)).effect(task.attacker, entity, task.attacker);
	}

}
