package com.shadowgame.rpg.modules.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.shadowgame.rpg.data.MapData;
import com.shadowgame.rpg.modules.core.AbstractFighter;
import com.shadowgame.rpg.modules.core.MapObject;
import com.shadowgame.rpg.modules.core.Monster;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.msg.map_12.Sc_12001;
import com.shadowgame.rpg.persist.entity.TMonster;
import com.shadowgame.rpg.service.Services;

/**
 * 地图副本
 * @author wcj10051891@gmail.com
 * @Date 2015年5月25日 下午7:04:12
 */
public class MapInstance {
	/**
	 * 当前地图副本序号id
	 */
	private int id;
	/**
	 * 副本所属地图
	 */
	private GameMap gameMap;
	/**
	 * 地图分块
	 */
	private ConcurrentHashMap<String, MapRegion> regions = new ConcurrentHashMap<String, MapRegion>();
	/**
	 * 地图内的对象
	 */
	private ConcurrentHashMap<Integer, MapObject> objects = new ConcurrentHashMap<Integer, MapObject>();
	/**
	 * 副本销毁任务
	 */
	private Future<?> destroyTask;

	public MapInstance(GameMap gameMap) {
		this.id = gameMap.getWorld().nextInstanceId();
		this.gameMap = gameMap;
		this.init();
	}

	/**
	 * 返回所属WorldMap
	 * @return parent
	 */
	public GameMap getGameMap() {
		return gameMap;
	}

	/**
	 * 查找对象所在区块
	 * @return a MapRegion
	 */
	public MapRegion getRegion(MapObject object) {
		Point point = object.getPosition().getPoint();
		return getRegion(point.x, point.y);
	}

	/**
	 * 查找对象所在区块
	 * @param x
	 * @param y
	 * @return a MapRegion
	 */
	public MapRegion getRegion(int pointX, int pointY) {
		return regions.get(getRegionId(pointX, pointY));
	}

	/**
	 * 返回x,y对应的区块id
	 * @param x
	 * @param y
	 * @return region id.
	 */
	private String getRegionId(int pointX, int pointY) {
		return pointX / MapRegion.width + "_" + pointY / MapRegion.height;
	}

	/**
	 * 创建所有区块，每个区块引用周围8块，一起9块合成一个广播区域
	 */
	private void init() {
		this.gameMap.addInstance(this);
		
		//初始化视野区域
		int rows = this.gameMap.entity.height / MapRegion.height;
		int columns = this.gameMap.entity.width / MapRegion.width;
		for (int y = 0; y < rows; y++)
			for (int x = 0; x < columns; x++) {
				String id = x + "_" + y;
				regions.put(id, new MapRegion(id, this));
			}
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < columns; x++) {
				MapRegion current = regions.get(x + "_" + y);
				int x1 = x - 1;
				int x2 = x + 1;
				int y1 = y - 1;
				int y2 = y + 1;
				if (x1 >= 0)
					current.addNeighbourRegion(regions.get(x1 + "_" + y));
				if (x2 >= 0)
					current.addNeighbourRegion(regions.get(x2 + "_" + y));
				if (y1 >= 0) {
					current.addNeighbourRegion(regions.get(x + "_" + y1));
					if (x1 >= 0)
						current.addNeighbourRegion(regions.get(x1 + "_" + y1));
					if (x2 >= 0)
						current.addNeighbourRegion(regions.get(x2 + "_" + y1));
				}
				if (y2 >= 0) {
					current.addNeighbourRegion(regions.get(x + "_" + y2));
					if (x1 >= 0)
						current.addNeighbourRegion(regions.get(x1 + "_" + y2));
					if (x2 >= 0)
						current.addNeighbourRegion(regions.get(x2 + "_" + y2));
				}
			}
		}
		//初始化怪物
		for (TMonster e : Services.data.get(MapData.class).monsters.get(this.gameMap.entity.id)) {
			add(new Monster(new Position(e.x, e.y), e), e.x, e.y);
		}
		//设置定时销毁
//		setDestroyTime(destroySecond);
	}

	
	/**
	 * 获取指定坐标点开始，格子数量gridRadius为半径内的所有格子所占的区块
	 */
	public Collection<MapRegion> getRegionsByGridRadius(int pointX, int pointY, int gridRadius) {
		List<Grid> grids = this.gameMap.getGridsByGridRadius(pointX, pointY, gridRadius);
		if(grids.isEmpty())
			return Collections.emptyList();
		Map<String, MapRegion> mapRegions = new LinkedHashMap<String, MapRegion>();
		for (Grid grid : grids) {
			MapRegion region = this.getRegion(grid.center.x, grid.center.y);
			mapRegions.put(region.getRegionId(), region);
		}
		return mapRegions.values();
	}

	/**
	 * 获得指定点周围多少格子半径内的Fighter
	 */
	public Collection<AbstractFighter> getFightersByGridRadius(int pointX, int pointY, int gridRadius) {
		Collection<MapRegion> regions = getRegionsByGridRadius(pointX, pointY, gridRadius);
		if(regions.isEmpty())
			return Collections.emptyList();
		List<AbstractFighter> fighters = new ArrayList<AbstractFighter>();
		for (MapRegion mapRegion : regions) {
			fighters.addAll(mapRegion.getFighters());
		}
		return fighters;
	}

	/**
	 * 获取指定坐标点开始，radius像素为半径内的所有格子所占的区块
	 */
	public Collection<MapRegion> getRegionsByRadius(int pointX, int pointY, int radius) {
		List<Grid> grids = this.gameMap.getGridsByRadius(pointX, pointY, radius);
		if(grids.isEmpty())
			return Collections.emptyList();
		Map<String, MapRegion> mapRegions = new LinkedHashMap<String, MapRegion>();
		for (Grid grid : grids) {
			MapRegion region = this.getRegion(grid.center.x, grid.center.y);
			mapRegions.put(region.getRegionId(), region);
		}
		return mapRegions.values();
	}
	
	/**
	 * 获得指定点周围多少像素半径内的Fighter
	 */
	public Collection<AbstractFighter> getFightersByRadius(int pointX, int pointY, int radius) {
		Collection<MapRegion> regions = getRegionsByRadius(pointX, pointY, radius);
		if(regions.isEmpty())
			return Collections.emptyList();
		List<AbstractFighter> fighters = new ArrayList<AbstractFighter>();
		for (MapRegion mapRegion : regions) {
			fighters.addAll(mapRegion.getFighters());
		}
		return fighters;
	}
	
	public void destory() {
		this.gameMap.removeInstance(this);
//		for (MapRegion r : regions.values()) {
//			for (Player player : r.getMapObjectByType(Player.class)) {
//				
//			}
//		}
		
//		for(Iterator<MapObject> it = objects.values().iterator();it.hasNext();) {
//			MapObject obj = it.next();
//			if(obj instanceof Player) {			
//				Player player = (Player) obj;
//				moveToEntryPoint((Player) obj, portal, true);
//			} else {
//				obj.getController().delete();
//			}
//				
//		}
	}
	
	/**
	 * 往该地图上添加对象，会设置该对象最新位置，并同步给周围的其他对象
	 * @param object
	 * @param pointX
	 * @param pointY
	 */
	public void add(MapObject object, int pointX, int pointY) {
		Position pos = object.getPosition();
		if(pos == null) {
			pos = new Position(pointX, pointY);
			object.setPosition(pos);
		}
		pos.update(object, this, pointX, pointY);
	}
	
	void addObject(MapObject object) {
		this.objects.put(object.getObjectId(), object);
	}
	
	void removeObject(Integer objectId) {
		this.objects.remove(objectId);
	}
	
	public MapObject findObject(Integer objectId) {
		return this.objects.get(objectId);
	}
	
	/**
	 * 将指定对象从该地图移除，即从该对象所属区块中移除，会同步给周围的其他对象
	 * @param object
	 */
	public void remove(MapObject object) {
		object.getPosition().getMapRegion().remove(object);
	}

	void onEnter(Player player) {
		player.send(new Sc_12001().from(this));
	}
	
	void onLeave(Player player) {
		
	}

	/**
	 * 获取副本序号id
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * 设置副本销毁
	 */
	private void setDestroyTime(int destroySecond) {
		if (destroyTask != null)
			destroyTask.cancel(true);

		destroyTask = Services.timerService.jdkScheduler.schedule(new Runnable() {
			@Override
			public void run() {
				destory();
			}
		}, destroySecond, TimeUnit.SECONDS);
	}
	
	@Override
	public String toString() {
		return "mapModelId:" + this.gameMap.getKey() + ",mapId:" + this.id;
	}
}
