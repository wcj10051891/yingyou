package com.shadowgame.rpg.modules.skill;

import xgame.core.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shadowgame.rpg.data.SkillData;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.service.Services;

public class PlayerSkillList extends SkillList {

	public PlayerSkillList(Player player) {
		super(player);
		if(StringUtils.hasText(player.entity.skill)) {
			for (JSONObject data : JSON.parseArray(player.entity.skill, JSONObject.class)) {
				int skillId = data.getIntValue("id");
				this.skills.put(skillId, new FighterSkill(player, Services.data.get(SkillData.class).getSkillEntity(skillId), data.getIntValue("lv")));
			}
		}
	}
	
	public void learnSkill(int skillId) {
		if(!this.skills.containsKey(skillId))
			this.skills.put(skillId, new FighterSkill(fighter, Services.data.get(SkillData.class).getSkillEntity(skillId), 1));
	}
	
	@Override
	public String toString() {
		JSONArray json = new JSONArray();
		for (FighterSkill skill : this.skills.values())
			json.add(skill.toJson());
		return json.toString();
	}
}