package com.shadowgame.rpg.modules.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import xgame.core.cache.AbstractCacheObject;
import xgame.core.cache.CacheObject;

import com.shadowgame.rpg.persist.dao.TGameMapDao;
import com.shadowgame.rpg.persist.entity.TGameMap;
import com.shadowgame.rpg.service.Services;

/**
 * 地图
 * @author wcj10051891@gmail.com
 * @Date 2015年5月25日 下午6:58:41
 */
public class GameMap extends AbstractCacheObject<Integer, TGameMap> {
	private static final TGameMapDao dao = Services.daoFactory.get(TGameMapDao.class);
	public TGameMap entity;
	private World world;
	public Map<String, Grid> grids;
	/**
	 * 地图的所有副本
	 */
	private Map<Integer, MapInstance> instances = new ConcurrentHashMap<Integer, MapInstance>();
	/**
	 * 第一个地图副本作为默认副本
	 */
	private MapInstance defaultInstance;

	/**
	 * 获取世界
	 * @return
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * 新建一个副本
	 * @return instance
	 */
	public MapInstance createInstance() {
		MapInstance instance = new MapInstance(this);
		//设置定时销毁
		return instance;
	}
	
	public void addInstance(MapInstance instance) {
		instances.put(instance.getId(), instance);
		world.mapInstances.put(instance.getId(), instance);
	}

	public MapInstance getInstance(int instanceId) {
		return instances.get(instanceId);
	}

	public void removeInstance(MapInstance instance) {
		this.instances.remove(instance.getId());
		this.world.mapInstances.remove(instance.getId());
	}
	
	public MapInstance getDefaultInstance() {
		return this.defaultInstance;
	}

	@Override
	public TGameMap get(Integer key) {
		return dao.get(key);
	}


	@Override
	public CacheObject<Integer, TGameMap> init(
			TGameMap entity,
			Object... contextParam) {
		this.entity = entity;
		this.world = (World)contextParam[0];
		//初始化地图阻挡
		int gridRows = entity.height / Grid.SIZE;
		int gridCols = entity.width / Grid.SIZE;
		grids = new HashMap<String, Grid>();
		for (int x = 0; x < gridCols; x++) {
			for (int y = 0; y < gridRows; y++)
				grids.put(x +"_" + y, new Grid(x, y, new Point(x * Grid.SIZE + Grid.SIZE / 2, y * Grid.SIZE + Grid.SIZE / 2), false));
		}
		
		if(this.defaultInstance == null)
			this.defaultInstance = createInstance();
		return this;
	}

	@Override
	public Integer getKey() {
		return entity.getId();
	}
	
	public Grid getGridByMapPoint(int pointX, int pointY) {
		return getGridByGridXY(pointX / Grid.SIZE, pointY / Grid.SIZE);
	}
	
	public Grid getGridByGridXY(int gridX, int gridY) {
		return this.grids.get(gridX + "_" + gridY);
	}
	
	public List<Grid> getRoundGrids(Grid current) {
		List<Grid> result = new ArrayList<Grid>();
		for(int x = current.x - 1; x <= current.x + 1; x++) {
			if(x < 0)
				continue;
			for(int y = current.y - 1; y <= current.y + 1; y++) {
				if(y < 0)
					continue;
				if(x == current.x && y == current.y)
					continue;
				result.add(getGridByGridXY(x, y));
			}
		}
		return result;
	}
	
	/**
	 * 获取指定坐标点开始，格子数量gridRadius为半径内的所有格子，即指定坐标点周围多少格半径内
	 */
	public List<Grid> getGridsByGridRadius(int pointX, int pointY, int gridRadius) {
		List<Grid> result = new ArrayList<Grid>();
		int round = 2 * gridRadius + 1;
		int num = round * round;
		Grid current = getGridByMapPoint(pointX, pointY);
		for (int i = 0; i < num; i++) {
			int gridX = current.x + i % round - gridRadius;
			int gridY = current.y + i / round - gridRadius;
			Grid grid = getGridByGridXY(gridX, gridY);
			if(grid != null)
				result.add(grid);
		}
		return result;
	}

	/**
	 * 获取指定坐标点开始，radius像素为半径内的所有格子，即指定坐标点周围多少像素半径内
	 */
	public List<Grid> getGridsByRadius(int pointX, int pointY, int radius) {
		List<Grid> grids = new ArrayList<Grid>();
		int x = pointX - radius;
		if (x < 0)
			x = 0;
		int left = x / Grid.SIZE;
		x = pointX + radius;
		int right = x / Grid.SIZE;
		int y = pointY - radius;
		if (y < 0)
			y = 0;
		int top = y / Grid.SIZE;
		y = pointY + radius;
		int buttom = y / Grid.SIZE;

		for (int i = top; i <= buttom; i++) {
			for (int j = left; j <= right; j++) {
				Grid grid = getGridByGridXY(i, j);
				if(grid != null)
					grids.add(grid);
			}
		}
		return grids;
	}
}
