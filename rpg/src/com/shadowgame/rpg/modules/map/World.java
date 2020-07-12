package com.shadowgame.rpg.modules.map;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.data.MapData;
import com.shadowgame.rpg.modules.player.PlayerContainer;
import com.shadowgame.rpg.persist.entity.TGameMap;
import com.shadowgame.rpg.service.Services;

/**
 * 世界对象
 * 
 * @author wcj10051891@gmail.com
 * @Date 2015年5月25日 下午6:57:59
 */
public class World {
	private static final Logger log = LoggerFactory.getLogger(World.class);

	public static final String GROUP_NAME = "World";
	
	public PlayerContainer allPlayers = new PlayerContainer();
	/**
	 * 地图集合，地图配置id->地图对象(包括多个该对象创建出的实际场景)
	 */
	public ConcurrentHashMap<Integer, GameMap> gameMaps = new ConcurrentHashMap<Integer, GameMap>();
	/**
	 * 实际场景集合
	 */
	public ConcurrentHashMap<Integer, MapInstance> mapInstances = new ConcurrentHashMap<Integer, MapInstance>();
	
	public World() {
		for (TGameMap entity : Services.data.get(MapData.class).maps.values())
			gameMaps.put(entity.id, Services.cacheService.get(entity.id, GameMap.class, true, this));
	}
	
	public int nextInstanceId() {
		synchronized (mapInstances) {
			int current = 0;
			do {
				current = (int)(System.currentTimeMillis() / 1000l);
			} while(this.mapInstances.contains(current));
			return current;
		}
	}

	public GameMap getWorldMap(int id) {
		GameMap map = gameMaps.get(id);
		if (map == null)
			throw new AppException("Map: " + id + " not exist");
		return map;
	}

	/**
	 * 更新可见对象位置，计算是否切换区域
	 */
	public void updatePosition(MapObject object, MapInstance mapInstance, int newX, int newY) {
		mapInstance.add(object, newX, newY);
	}
	
	/**
	 * 更新可见对象位置，计算是否切换区域
	 */
	public void updatePosition(MapObject object, int newX, int newY) {
		updatePosition(object, object.getPosition().getMapRegion().getMapInstance(), newX, newY);
	}
}
