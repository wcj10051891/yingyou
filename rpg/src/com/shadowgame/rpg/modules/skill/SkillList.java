package com.shadowgame.rpg.modules.skill;

import java.util.HashMap;
import java.util.Map;

import com.shadowgame.rpg.core.NoticeException;
import com.shadowgame.rpg.modules.fight.AbstractFighter;
import com.shadowgame.rpg.modules.map.MapObject;

public class SkillList {
	protected AbstractFighter fighter;
	protected Map<Integer, FighterSkill> skills = new HashMap<Integer, FighterSkill>();

	public SkillList(AbstractFighter fighter) {
		this.fighter = fighter;
	}

	public void learnSkill(int skillId) {
		
	}
	
	public FighterSkill getSkill(int skillId) {
		return skills.get(skillId);
	}
	
	public FighterSkill getDefaultSkill() {
		if(this.skills.isEmpty())
			return null;
		return skills.values().iterator().next();
	}
	
	public void useSkill(int skillId, int targetId) {
		if(!this.skills.containsKey(skillId))
			throw new NoticeException("未学习该技能");
		
		MapObject target = fighter.getPosition().getMapInstance().getObject(targetId);
		if(target == null)
			throw new NoticeException("未找到目标");
		if(!(target instanceof AbstractFighter))
			throw new NoticeException("不能对目标使用技能");
		skills.get(skillId).fire((AbstractFighter)target);
	}

	public AbstractFighter getFighter() {
		return fighter;
	}
}
