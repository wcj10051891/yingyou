package com.shadowgame.rpg.modules.mission;

import java.util.HashMap;
import java.util.Map;

import com.shadowgame.rpg.persist.dao.MissionDao;
import com.shadowgame.rpg.service.Services;

public class MissionConfig {
	private MissionDao dao = Services.daoFactory.get(MissionDao.class);
	public Map<Integer, Mission> missions = new HashMap<Integer, Mission>();
	
	public MissionConfig() {
		for (com.shadowgame.rpg.persist.entity.Mission entity : dao.getAll()) {
			this.missions.put(entity.id, new Mission(entity));
		}
	}
}
