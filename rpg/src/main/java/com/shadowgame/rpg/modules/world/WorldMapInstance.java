package com.shadowgame.rpg.modules.world;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.modules.core.VisibleObject;

/**
 * 地图副本
 * @author wcj10051891@gmail.com
 * @Date 2015年5月25日 下午7:04:12
 */
public class WorldMapInstance {
	/**
	 * 当前地图副本序号id
	 */
	private int instanceId;
	/**
	 * 副本所属地图
	 */
	private WorldMap worldMap;
	/**
	 * 宽
	 */
	private int width;
	/**
	 * 高
	 */
	private int height;
	/**
	 * 地图分块
	 */
	private ConcurrentHashMap<String, MapRegion> regions = new ConcurrentHashMap<String, MapRegion>();
	/**
	 * 当前地图上的所有对象
	 */
	private ConcurrentHashMap<Long, VisibleObject> worldMapObjects = new ConcurrentHashMap<Long, VisibleObject>();
	/**
	 * 当前地图上所有玩家
	 */
	private ConcurrentHashMap<Long, Player> worldMapPlayers = new ConcurrentHashMap<Long, Player>();
	/**
	 * 副本销毁任务
	 */
	private Future<?> destroyTask;

	public WorldMapInstance(WorldMap worldMap, int instanceId) {
		this.worldMap = worldMap;
		this.instanceId = instanceId;
	}

	/**
	 * 返回mapId
	 * @return world map id
	 */
	public int getMapId() {
		return getWorldMap().getMapId();
	}

	/**
	 * 返回所属WorldMap
	 * @return parent
	 */
	public WorldMap getWorldMap() {
		return worldMap;
	}

	/**
	 * 查找对象所在区块
	 * @return a MapRegion
	 */
	MapRegion getRegion(VisibleObject object) {
		return getRegion(object.getX(), object.getY());
	}

	/**
	 * 查找对象所在区块
	 * @param x
	 * @param y
	 * @return a MapRegion
	 */
	MapRegion getRegion(int x, int y) {
		return regions.get(getRegionId(x, y));
	}

	/**
	 * 返回x,y对应的区块id
	 * @param x
	 * @param y
	 * @return region id.
	 */
	private String getRegionId(int x, int y) {
		return x / width + "_" + y / height;
	}

	/**
	 * 创建所有区块，每个区块引用周围8块，一起9块合成一个广播区域
	 */
	public void init() {
		int rows = height / MapRegion.height;
		int columns = width / MapRegion.width;
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				String id = r + "_" + c;
				regions.put(id, new MapRegion(id, this));
			}
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				MapRegion current = regions.get(r + "_" + c);
				int r1 = r - 1;
				int r2 = r + 1;
				int c1 = c - 1;
				int c2 = c + 1;
				if (r1 >= 0) {
					current.addNeighbourRegion(regions.get(r1 + "_" + c));
					if (c1 >= 0)
						current.addNeighbourRegion(regions.get(r1 + "_" + c1));
					if (c2 >= 0)
						current.addNeighbourRegion(regions.get(r1 + "_" + c2));
				}
				if (r2 >= 0) {
					current.addNeighbourRegion(regions.get(r2 + "_" + c));
					if (c1 >= 0)
						current.addNeighbourRegion(regions.get(r2 + "_" + c1));
					if (c2 >= 0)
						current.addNeighbourRegion(regions.get(r2 + "_" + c2));
				}
				if (c1 >= 0)
					current.addNeighbourRegion(regions.get(r + "_" + c1));
				if (c2 >= 0)
					current.addNeighbourRegion(regions.get(r + "_" + c2));
			}
	}

	/**
	 * 返回所属世界
	 * @return World
	 */
	public World getWorld() {
		return getWorldMap().getWorld();
	}

	/**
	 * 添加对象
	 */
	public void addObject(VisibleObject object) {
		if (worldMapObjects.put(object.getObjectId(), object) == null) {
			if (object instanceof Player)
				worldMapPlayers.put(object.getObjectId(), (Player) object);
		}
	}

	/**
	 * 移除对象
	 */
	public void removeObject(VisibleObject object) {
		worldMapObjects.remove(object.getObjectId());
		if (object instanceof Player)
			worldMapPlayers.remove(object.getObjectId());
	}

	/**
	 * 获取副本序号id
	 * @return
	 */
	public int getInstanceId() {
		return instanceId;
	}

	/**
	 * 检查玩家是否在这里
	 * @param objectId
	 * @return
	 */
	public boolean isInInstance(Long objectId) {
		return worldMapPlayers.containsKey(objectId);
	}

	/**
	 * 当前地图副本上所有对象
	 * @return
	 */
	public Iterator<VisibleObject> objectIterator() {
		return worldMapObjects.values().iterator();
	}

	/**
	 * 当前地图副本上所有玩家
	 * @return
	 */
	public Iterator<Player> playerIterator() {
		return worldMapPlayers.values().iterator();
	}

	public Future<?> getDestroyTask() {
		return destroyTask;
	}

	public void setDestroyTask(Future<?> destroyTask) {
		this.destroyTask = destroyTask;
	}

}
