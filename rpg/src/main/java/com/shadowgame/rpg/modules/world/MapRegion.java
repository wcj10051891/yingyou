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
	 * Region id of this map region
	 */
	private String regionId;
	/**
	 * WorldMapInstance witch is parent of this map region.
	 */
	private WorldMapInstance parent;
	/**
	 * Surrounding regions + self.
	 */
	private List<MapRegion> neighbours = new ArrayList<MapRegion>(9);
	/**
	 * Objects on this map region.
	 */
	private Map<Long, VisibleObject> objects = new ConcurrentHashMap<Long, VisibleObject>();

	MapRegion(String id, WorldMapInstance parent) {
		this.regionId = id;
		this.parent = parent;
		this.neighbours.add(this);
	}

	/**
	 * Return World map id.
	 * 
	 * @return world map id
	 */
	public int getMapId() {
		return parent.getMapId();
	}

	/**
	 * Return an instance of {@link World}, which keeps map, to which belongs
	 * this region
	 */
	public World getWorld() {
		return parent.getWorld();
	}

	/**
	 * Returns region id of this map region. [NOT WORLD ID!]
	 * 
	 * @return region id.
	 */
	public String getRegionId() {
		return regionId;
	}

	/**
	 * Returns WorldMapInstance witch is parent of this instance
	 * 
	 * @return parent
	 */
	public WorldMapInstance getParent() {
		return parent;
	}

	/**
	 * Returns neighbours regions
	 * @return neighbours iterator
	 */
	public List<MapRegion> getNeighbours() {
		return neighbours;
	}

	/**
	 * Returns iterator over AionObjects on this region
	 * 
	 * @return objects iterator
	 */
	public Collection<VisibleObject> getObjects() {
		return objects.values();
	}

	/**
	 * Add neighbour region to this region neighbours list.
	 * 
	 * @param neighbour
	 */
	void addNeighbourRegion(MapRegion neighbour) {
		neighbours.add(neighbour);
	}

	/**
	 * Add AionObject to this region objects list.
	 * 
	 * @param object
	 */
	void add(VisibleObject object) {
		objects.put(object.getObjectId(), object);
	}

	/**
	 * Remove AionObject from region objects list.
	 * 
	 * @param object
	 */
	void remove(VisibleObject object) {
		objects.remove(object.getObjectId());
	}
}
