package com.shadowgame.rpg.data;

import java.util.HashMap;
import java.util.Map;

import xgame.core.util.MultiValueMap;

import com.shadowgame.rpg.core.Data;
import com.shadowgame.rpg.persist.dao.TGameMapDao;
import com.shadowgame.rpg.persist.dao.TMonsterDao;
import com.shadowgame.rpg.persist.entity.TGameMap;
import com.shadowgame.rpg.persist.entity.TMonster;
import com.shadowgame.rpg.service.Services;

@Data
public class MapData {
	private static final TGameMapDao mapDao = Services.daoFactory.get(TGameMapDao.class);
	private static final TMonsterDao monsterDao = Services.daoFactory.get(TMonsterDao.class);
	public Map<Integer, TGameMap> maps = new HashMap<Integer, TGameMap>();
	public MultiValueMap<Integer, TMonster> monsters = new MultiValueMap<Integer, TMonster>();
	public MapData() {
		for (TGameMap m : mapDao.getAll())
			this.maps.put(m.id, m);
		
		for (TMonster t : monsterDao.getAll())
			monsters.add(t.mapId, t);
	}
}
