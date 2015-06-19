package com.shadowgame.rpg.modules.mission;

import java.util.HashMap;
import java.util.Map;

import xgame.core.util.ObjectUtils;

import com.alibaba.fastjson.JSONObject;
import com.shadowgame.rpg.persist.dao.TMissionGoalDao;
import com.shadowgame.rpg.persist.entity.TMissionGoal;
import com.shadowgame.rpg.service.Services;

public class MissionGoalConfig {
	private TMissionGoalDao dao = Services.daoFactory.get(TMissionGoalDao.class);

	public Map<String, Class<?>> goals = new HashMap<String, Class<?>>();
	public Map<String, TMissionGoal> entitys = new HashMap<String, TMissionGoal>();

	public MissionGoalConfig() {
		for (TMissionGoal entity : dao.getAll()) {
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
