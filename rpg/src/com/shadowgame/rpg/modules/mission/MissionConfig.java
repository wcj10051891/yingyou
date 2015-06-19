package com.shadowgame.rpg.modules.mission;

import java.util.HashMap;
import java.util.Map;

import com.shadowgame.rpg.persist.dao.TMissionDao;
import com.shadowgame.rpg.persist.entity.TMission;
import com.shadowgame.rpg.service.Services;

public class MissionConfig {
	private TMissionDao dao = Services.daoFactory.get(TMissionDao.class);
	public Map<Integer, Mission> missions = new HashMap<Integer, Mission>();
	
	public MissionConfig() {
		for (TMission entity : dao.getAll()) {
			this.missions.put(entity.id, new Mission(entity));
		}
	}
}
