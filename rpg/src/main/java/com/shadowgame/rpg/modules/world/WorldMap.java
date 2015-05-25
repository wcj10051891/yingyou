package com.shadowgame.rpg.modules.world;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shadowgame.rpg.core.AppException;

/**
 * 地图
 * @author wcj10051891@gmail.com
 * @Date 2015年5月25日 下午6:58:41
 */
public class WorldMap {
	private int mapId;
	private String name;
	private World world;
	private Map<Integer, WorldMapInstance> instances = new ConcurrentHashMap<Integer, WorldMapInstance>();
	/**
	 * 副本个数
	 */
	private int instanceCount = 1;
	private int nextInstanceId = 1;

	public WorldMap(World world) {
		this.world = world;
		if (instanceCount != 0) {
			for (int i = 1; i <= instanceCount; i++)
				addInstance(nextInstanceId, new WorldMapInstance(this,
						nextInstanceId));
		} else {
			addInstance(nextInstanceId, new WorldMapInstance(this,
					nextInstanceId));
		}
	}

	/**
	 * Returns map id
	 * 
	 * @return map id
	 */
	public int getMapId() {
		return mapId;
	}

	public String getName() {
		return name;
	}

	public World getWorld() {
		return world;
	}

	/**
	 * 
	 * @return int
	 */
	public int getInstanceCount() {
		return instanceCount > 0 ? instanceCount : 1;
	}

	public void addInstance(int instanceId, WorldMapInstance instance) {
		instances.put(instanceId, instance);
		nextInstanceId++;
	}

	public int getNextInstanceId() {
		return nextInstanceId;
	}

	public WorldMapInstance getWorldMapInstanceById(int instanceId) {
		if (instanceCount != 0) {
			if (instanceId > instanceCount)
				throw new AppException("WorldMapInstance " + this.mapId
						+ " has lower instances count than " + instanceId);
		}
		return instances.get(instanceId);
	}

	public void removeWorldMapInstance(int instanceId) {
		instances.remove(instanceId);
	}

}
