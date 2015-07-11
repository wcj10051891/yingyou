package com.shadowgame.rpg.data;

import java.util.Map;

import xgame.core.util.MultiValueMap;

import com.shadowgame.rpg.core.AbstractData;
import com.shadowgame.rpg.persist.dao.TGameMapDao;
import com.shadowgame.rpg.persist.dao.TMonsterDao;
import com.shadowgame.rpg.persist.entity.TGameMap;
import com.shadowgame.rpg.persist.entity.TMonster;
import com.shadowgame.rpg.service.Services;

public class MapData extends AbstractData {
	private static final TGameMapDao mapDao = Services.daoFactory.get(TGameMapDao.class);
	private static final TMonsterDao monsterDao = Services.daoFactory.get(TMonsterDao.class);
	public Map<Integer, TGameMap> maps = new EntityMap<Integer, TGameMap>();
	public Map<Integer, TMonster> monsters = new EntityMap<Integer, TMonster>();
	public MultiValueMap<Integer, TMonster> mapMonsters = new MultiValueMap<Integer, TMonster>();
	
	public void load() {
		for (TGameMap m : mapDao.getAll())
			this.maps.put(m.id, m);
		
		for (TMonster t : monsterDao.getAll())
			monsters.put(t.mapId, t);
		
		for (TMonster m : monsters.values())
			mapMonsters.add(m.getMapId(), m);
	}
}
