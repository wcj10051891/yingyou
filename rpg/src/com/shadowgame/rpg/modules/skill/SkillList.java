package com.shadowgame.rpg.modules.skill;

import java.util.HashMap;
import java.util.Map;

import com.shadowgame.rpg.core.NoticeException;
import com.shadowgame.rpg.data.SkillData;
import com.shadowgame.rpg.modules.core.AbstractFighter;
import com.shadowgame.rpg.modules.core.MapObject;
import com.shadowgame.rpg.service.Services;

public class SkillList {
	protected AbstractFighter fighter;
	protected Map<Integer, FighterSkill> skills = new HashMap<Integer, FighterSkill>();

	public SkillList(AbstractFighter fighter) {
		this.fighter = fighter;
	}

	public void learnSkill(int skillId) {
		
	}
	
	public void useSkill(int skillId, int targetId) {
		if(!this.skills.containsKey(skillId))
			throw new NoticeException("未学习该技能");
		
		MapObject target = fighter.getPosition().getMapInstance().findObject(targetId);
		if(target == null)
			throw new NoticeException("未找到目标");
		if(!(target instanceof AbstractFighter))
			throw new NoticeException("不能对目标使用技能");
			
		Services.data.get(SkillData.class).getSkillLogic(skillId).fire(
				fighter, (AbstractFighter)target);
	}

	public AbstractFighter getFighter() {
		return fighter;
	}

}
