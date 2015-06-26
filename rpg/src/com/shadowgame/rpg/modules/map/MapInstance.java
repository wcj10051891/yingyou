package com.shadowgame.rpg.modules.map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.shadowgame.rpg.modules.core.MapObject;
import com.shadowgame.rpg.modules.core.Monster;
import com.shadowgame.rpg.modules.core.Player;
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
		for (TMonster e : Services.config.mapConfig.monsters.get(this.gameMap.entity.id)) {
			add(new Monster(new Position(e.x, e.y), e), e.x, e.y);
		}
		//设置定时销毁
//		setDestroyTime(destroySecond);
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
	
	public void add(MapObject object, int pointX, int pointY) {
		Position pos = object.getPosition();
		if(pos == null) {
			pos = new Position(pointX, pointY);
			object.setPosition(pos);
		}
		pos.update(object, this, pointX, pointY);
	}
	
	public void remove(MapObject object) {
		object.getPosition().getMapRegion().remove(object);
	}

	public void onEnter(Player player) {
		
	}
	
	public void onLeave(Player player) {
		
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
