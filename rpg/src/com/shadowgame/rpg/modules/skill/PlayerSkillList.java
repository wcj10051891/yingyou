package com.shadowgame.rpg.modules.skill;

import java.util.List;

import xgame.core.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shadowgame.rpg.data.SkillData;
import com.shadowgame.rpg.modules.player.Player;
import com.shadowgame.rpg.persist.entity.TSkill;
import com.shadowgame.rpg.service.Services;

public class PlayerSkillList extends SkillList {

	public PlayerSkillList(Player player) {
		super(player);
		if(StringUtils.hasText(player.entity.skill)) {
			List<JSONObject> skills = JSON.parseArray(player.entity.skill, JSONObject.class);
			if(skills.isEmpty()) {//添加默认技能
				for (TSkill tSkill : Services.data.get(SkillData.class).getSkillsByVocation(player.entity.vocation))
					this.skills.put(tSkill.id, new FighterSkill(player, tSkill, 1));
			} else {
				for (JSONObject data : skills) {
					int skillId = data.getIntValue("id");
					this.skills.put(skillId, new FighterSkill(player, Services.data.get(SkillData.class).getSkillEntity(skillId), data.getIntValue("lv")));
				}
			}
		}
	}
	
	@Override
	public String toString() {
		JSONArray json = new JSONArray();
		for (FighterSkill skill : this.skills.values())
			json.add(skill.toJson());
		return json.toString();
	}
}