package com.shadowgame.rpg.modules.mission;

import java.util.HashMap;
import java.util.Map;

import xgame.core.util.ObjectUtils;

import com.alibaba.fastjson.JSONObject;
import com.shadowgame.rpg.persist.dao.MissionGoalDao;
import com.shadowgame.rpg.service.Services;

public class MissionGoalConfig {
	private MissionGoalDao dao = Services.daoFactory.get(MissionGoalDao.class);

	public Map<String, Class<?>> goals = new HashMap<String, Class<?>>();
	public Map<String, com.shadowgame.rpg.persist.entity.MissionGoal> entitys = new HashMap<String, com.shadowgame.rpg.persist.entity.MissionGoal>();

	public MissionGoalConfig() {
		for (com.shadowgame.rpg.persist.entity.MissionGoal entity : dao.getAll()) {
			entitys.put(entity.key, entity);
			goals.put(entity.key, ObjectUtils.forName(entity.clazz));
		}
	}
	@SuppressWarnings("unchecked")
	public <T extends MissionGoal<?>> T createMissionGoal(JSONObject goalJson, JSONObject param) {
		String key = goalJson.getString("key");
		return (T)ObjectUtils.create(goals.get(key),
				new Class[]{String.class, JSONObject.class, int.class},
				new Object[]{key, param, goalJson.getIntValue("current")});
	}
}
