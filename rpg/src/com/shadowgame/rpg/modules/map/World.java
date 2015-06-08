package com.shadowgame.rpg.modules.map;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.modules.core.MapObject;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.modules.core.PlayerContainer;

/**
 * 世界对象
 * 
 * @author wcj10051891@gmail.com
 * @Date 2015年5月25日 下午6:57:59
 */
public class World {
	private static final Logger log = LoggerFactory.getLogger(World.class);

	public PlayerContainer allPlayers = new PlayerContainer();
	/**
	 * 包括玩家在内的所有地图上的可见对象
	 */
	public ConcurrentHashMap<Long, MapObject> allObjects = new ConcurrentHashMap<Long, MapObject>();
	/**
	 * 地图集合
	 */
	public ConcurrentHashMap<Integer, GameMap> worldMaps = new ConcurrentHashMap<Integer, GameMap>();
	
	public World() {
		worldMaps.put(1, new GameMap(this));
	}

	public GameMap getWorldMap(int id) {
		GameMap map = worldMaps.get(id);
		if (map == null)
			throw new AppException("Map: " + id + " not exist!");
		return map;
	}

	public void addObject(MapObject object) {
		if (allObjects.putIfAbsent(object.getObjectId(), object) == null) {
			if (object instanceof Player)
				allPlayers.add((Player) object);
		}
	}

	public void removeObject(MapObject object) {
		allObjects.remove(object.getObjectId());
		if (object instanceof Player)
			allPlayers.remove((Player) object);
	}

	public MapObject findObject(Long objectId) {
		return allObjects.get(objectId);
	}

	/**
	 * 更新可见对象位置，计算是否切换区域
	 */
	public void updatePosition(MapObject object, MapInstance mapInstance, int newX, int newY) {
		object.getPosition().setXY(object, mapInstance, newX, newY);
	}
}
