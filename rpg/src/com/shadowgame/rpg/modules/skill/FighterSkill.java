package com.shadowgame.rpg.modules.skill;

import com.alibaba.fastjson.JSONObject;
import com.shadowgame.rpg.data.SkillData;
import com.shadowgame.rpg.modules.fight.AbstractFighter;
import com.shadowgame.rpg.persist.entity.TSkill;
import com.shadowgame.rpg.service.Services;


/**
 * 技能
 * @author wcj10051891@gmail.com
 * @date 2015年6月30日 下午5:10:31
 */
public class FighterSkill {
	public AbstractFighter fighter;
	public TSkill entity;
	public int lv;
	
	public FighterSkill(AbstractFighter fighter, TSkill entity, int lv) {
		super();
		this.fighter = fighter;
		this.entity = entity;
		this.lv = lv;
	}
	
	public JSONObject toJson() {
		JSONObject data = new JSONObject();
		data.put("id", entity.id);
		data.put("lv", lv);
		return data;
	}
	
	public void fire(AbstractFighter target) {
		new SkillTask(fighter, target, Services.data.get(SkillData.class).getSkillLogic(this.entity.id), this).start();
	}
	
	@Override
	public String toString() {
		return entity.name + "-lv" + this.lv;
	}
}
