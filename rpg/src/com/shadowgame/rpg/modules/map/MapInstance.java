package com.shadowgame.rpg.modules.map;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.shadowgame.rpg.modules.core.AbstractSpirit;
import com.shadowgame.rpg.modules.core.MapObject;
import com.shadowgame.rpg.modules.core.Player;
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
	private int instanceId;
	/**
	 * 副本所属地图
	 */
	private GameMap gameMap;
	/**
	 * 宽
	 */
	private int width = 1000;
	/**
	 * 高
	 */
	private int height = 1000;
	/**
	 * 地图分块
	 */
	private ConcurrentHashMap<String, MapRegion> regions = new ConcurrentHashMap<String, MapRegion>();
	/**
	 * 当前地图上的所有对象
	 */
	private ConcurrentHashMap<Long, MapObject> objects = new ConcurrentHashMap<Long, MapObject>();
	/**
	 * 当前地图上所有玩家
	 */
	private ConcurrentHashMap<Long, Player> players = new ConcurrentHashMap<Long, Player>();
	/**
	 * 副本销毁任务
	 */
	private Future<?> destroyTask;

	public MapInstance(GameMap gameMap, int instanceId) {
		this.gameMap = gameMap;
		this.instanceId = instanceId;
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
		return getRegion(object.getPosition().getPoint());
	}

	/**
	 * 查找对象所在区块
	 * @param x
	 * @param y
	 * @return a MapRegion
	 */
	public MapRegion getRegion(Point point) {
		return regions.get(getRegionId(point));
	}

	/**
	 * 返回x,y对应的区块id
	 * @param x
	 * @param y
	 * @return region id.
	 */
	private String getRegionId(Point point) {
		return point.x / MapRegion.width + "_" + point.y / MapRegion.height;
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
		//设置定时销毁
//		setDestroyTime(destroySecond);
	}
	
	public void destory() {
		gameMap.removeInstance(instanceId);
		for(Iterator<MapObject> it = objects.values().iterator();it.hasNext();) {
			MapObject obj = it.next();
			if(obj instanceof Player) {			
				Player player = (Player) obj;
//				moveToEntryPoint((Player) obj, portal, true);
			} else {
//				obj.getController().delete();
			}
				
		}
	}

	public void onEnter(AbstractSpirit spirit) {
		
	}
	
	public void onLeave(AbstractSpirit spirit) {
		
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
		return players.containsKey(objectId);
	}

	/**
	 * 当前地图副本上所有玩家
	 * @return
	 */
	public Iterator<Player> playerIterator() {
		return players.values().iterator();
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
}
