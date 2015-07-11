package com.shadowgame.rpg.data;

import java.util.HashMap;
import java.util.Map;

import com.shadowgame.rpg.core.AbstractData;
import com.shadowgame.rpg.modules.mission.Mission;
import com.shadowgame.rpg.persist.dao.TMissionDao;
import com.shadowgame.rpg.persist.entity.TMission;
import com.shadowgame.rpg.service.Services;

public class MissionData extends AbstractData {
	private TMissionDao dao = Services.daoFactory.get(TMissionDao.class);
	public Map<Integer, TMission> entitys = new EntityMap<Integer, TMission>();
	public Map<Integer, Mission> missions = new HashMap<Integer, Mission>();
	
	public void load() {
		for (TMission entity : dao.getAll()) {
			entitys.put(entity.id, entity);
			missions.put(entity.id, new Mission(entity));
		}
	}
}
