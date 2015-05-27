package com.shadowgame.rpg.modules.world;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.modules.core.PlayerContainer;
import com.shadowgame.rpg.modules.core.VisibleObject;

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
	public ConcurrentHashMap<Long, VisibleObject> allObjects = new ConcurrentHashMap<Long, VisibleObject>();
	/**
	 * 地图集合
	 */
	public ConcurrentHashMap<Integer, WorldMap> worldMaps = new ConcurrentHashMap<Integer, WorldMap>();

	public WorldMap getWorldMap(int id) {
		WorldMap map = worldMaps.get(id);
		if (map == null)
			throw new AppException("Map: " + id + " not exist!");
		return map;
	}

	public void addObject(VisibleObject object) {
		if (allObjects.putIfAbsent(object.getObjectId(), object) == null) {
			if (object instanceof Player)
				allPlayers.add((Player) object);
		}
	}

	public void removeObject(VisibleObject object) {
		allObjects.remove(object.getObjectId());
		if (object instanceof Player)
			allPlayers.remove((Player) object);
	}

	public VisibleObject findObject(Long objectId) {
		return allObjects.get(objectId);
	}

	/**
	 * 更新可见对象位置及视野
	 */
	public void updatePosition(VisibleObject object, int newX, int newY, boolean updateKnownlist) {
		object.getPosition().setXY(newX, newY);
		MapRegion oldRegion = object.getActiveRegion();
		if (oldRegion == null)
			throw new AppException("object " + object + "'s oldRegion is null.");

		MapRegion newRegion = oldRegion.getWorldMapInstance().getRegion(object);
		if (newRegion != oldRegion) {
			oldRegion.remove(object);
			newRegion.add(object);
			object.getPosition().setMapRegion(newRegion);
		}
		if (updateKnownlist)
			object.updateKnownlist();
	}

	public void setPosition(VisibleObject object, int mapId, int instance, int x, int y) {
		object.getPosition().setXY(x, y);
		object.getPosition().setMapRegion(
				getWorldMap(mapId).getWorldMapInstanceById(instance).getRegion(
						object));
	}
	
	public WorldPosition createPosition(int mapId, int x, int y)
	{
		WorldPosition position = new WorldPosition();
		position.setXY(x, y);
		position.setMapRegion(getWorldMap(mapId).getWorldMapInstance().getRegion(x, y));
		return position;
	}
}
