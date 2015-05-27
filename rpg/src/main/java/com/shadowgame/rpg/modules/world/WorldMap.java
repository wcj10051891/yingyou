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
	/**
	 * 是否是副本类型
	 */
	private boolean instanceType;
	private World world;
	/**
	 * 地图的所有副本
	 */
	private Map<Integer, WorldMapInstance> instances = new ConcurrentHashMap<Integer, WorldMapInstance>();
	/**
	 * 配置副本个数，有些非副本类型的地图，可以默认也开几个副本来分散玩家
	 */
	private int twinCount = 1;
	private int nextInstanceId = 1;

	public WorldMap(World world) {
		this.world = world;
		if (twinCount != 0) {
			for (int i = 1; i <= twinCount; i++)
				addInstance(nextInstanceId, new WorldMapInstance(this, nextInstanceId));
		} else {
			addInstance(nextInstanceId, new WorldMapInstance(this, nextInstanceId));
		}
	}

	/**
	 * 获取地图ID
	 * @return map id
	 */
	public int getMapId() {
		return mapId;
	}

	/**
	 * 获取地图名字
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取世界
	 * @return
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * 获取副本数量
	 * @return int
	 */
	public int getTwinCount() {
		return twinCount > 0 ? twinCount : 1;
	}

	/**
	 * 新建一个副本
	 * @param instanceId
	 * @param instance
	 */
	public void addInstance(int instanceId, WorldMapInstance instance) {
		instances.put(instanceId, instance);
		nextInstanceId++;
	}

	public int getNextInstanceId() {
		return nextInstanceId;
	}

	public WorldMapInstance getWorldMapInstanceById(int instanceId) {
		if (twinCount != 0) {
			if (instanceId > twinCount)
				throw new AppException("WorldMapInstance " + this.mapId
						+ " has lower instances count than " + instanceId);
		}
		return instances.get(instanceId);
	}

	public void removeWorldMapInstance(int instanceId) {
		instances.remove(instanceId);
	}
	
	public WorldMapInstance getWorldMapInstance() {
		return instances.get(1);
	}

	public boolean isInstanceType() {
		return instanceType;
	}
}
