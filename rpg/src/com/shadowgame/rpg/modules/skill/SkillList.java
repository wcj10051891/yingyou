package com.shadowgame.rpg.modules.skill;

import java.util.HashMap;
import java.util.Map;

import com.shadowgame.rpg.data.SkillData;
import com.shadowgame.rpg.modules.core.AbstractFighter;
import com.shadowgame.rpg.service.Services;

public class SkillList {
	protected AbstractFighter fighter;
	protected Map<Integer, FighterSkill> skills = new HashMap<Integer, FighterSkill>();

	public SkillList(AbstractFighter fighter) {
		this.fighter = fighter;
	}

	public void learnSkill(int skillId) {
		
	}
	
	public void useSkill(int skillId) {
		if(skills.containsKey(skillId)) {
			Services.data.get(SkillData.class).getSkillLogic(skillId).fire(
					fighter, fighter.getPosition().getDirection());
		}
	}

	public AbstractFighter getFighter() {
		return fighter;
	}

}
