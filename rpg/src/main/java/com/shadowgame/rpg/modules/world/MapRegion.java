package com.shadowgame.rpg.modules.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shadowgame.rpg.modules.core.VisibleObject;

/**
 * 地图区块
 * @author wcj10051891@gmail.com
 * @Date 2015年5月25日 下午7:17:29
 */
public class MapRegion {
	
	public static int height = 300;
	public static int width = 300;
	/**
	 * 区块id
	 */
	private String regionId;
	/**
	 * 所属地图
	 */
	private WorldMapInstance worldMapInstance;
	/**
	 * 包括当前区块在内，以及周围的区块一共9个区块
	 */
	private List<MapRegion> neighbours = new ArrayList<MapRegion>(9);
	/**
	 * 区块中的对象
	 */
	private Map<Long, VisibleObject> objects = new ConcurrentHashMap<Long, VisibleObject>();

	MapRegion(String id, WorldMapInstance worldMapInstance) {
		this.regionId = id;
		this.worldMapInstance = worldMapInstance;
		this.neighbours.add(this);
	}

	/**
	 * 获取所属地图id
	 * @return world map id
	 */
	public int getMapId() {
		return worldMapInstance.getMapId();
	}

	/**
	 * 获取世界
	 */
	public World getWorld() {
		return worldMapInstance.getWorld();
	}

	/**
	 * 获取当前区块id
	 * @return region id.
	 */
	public String getRegionId() {
		return regionId;
	}

	/**
	 * 获取所属地图
	 * @return parent
	 */
	public WorldMapInstance getWorldMapInstance() {
		return worldMapInstance;
	}

	/**
	 * 获取区块组
	 * @return neighbours iterator
	 */
	public List<MapRegion> getNeighbours() {
		return neighbours;
	}

	/**
	 * 获取区块中的所有对象
	 * @return objects iterator
	 */
	public Collection<VisibleObject> getObjects() {
		return objects.values();
	}

	/**
	 * 添加邻居区块
	 * @param neighbour
	 */
	void addNeighbourRegion(MapRegion neighbour) {
		neighbours.add(neighbour);
	}

	/**
	 * 添加对象
	 * @param object
	 */
	void add(VisibleObject object) {
		objects.put(object.getObjectId(), object);
	}

	/**
	 * 移除对象
	 * @param object
	 */
	void remove(VisibleObject object) {
		objects.remove(object.getObjectId());
	}
}
