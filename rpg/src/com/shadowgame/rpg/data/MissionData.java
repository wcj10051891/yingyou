package com.shadowgame.rpg.data;

import java.util.HashMap;
import java.util.Map;

import com.shadowgame.rpg.core.Data;
import com.shadowgame.rpg.modules.mission.Mission;
import com.shadowgame.rpg.persist.dao.TMissionDao;
import com.shadowgame.rpg.persist.entity.TMission;
import com.shadowgame.rpg.service.Services;

@Data
public class MissionData {
	private TMissionDao dao = Services.daoFactory.get(TMissionDao.class);
	public Map<Integer, Mission> missions = new HashMap<Integer, Mission>();
	
	public MissionData() {
		for (TMission entity : dao.getAll()) {
			this.missions.put(entity.id, new Mission(entity));
		}
	}
}
