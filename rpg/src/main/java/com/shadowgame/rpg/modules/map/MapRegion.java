package com.shadowgame.rpg.modules.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.shadowgame.rpg.modules.core.AbstractSpirit;
import com.shadowgame.rpg.modules.core.MapObject;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.net.msg.Message;

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
	 * 当前区域以及周围一起9个区块组成一个广播组，互相接收状态更新
	 */
	private String broadcastGroupName;
	/**
	 * 所属地图
	 */
	private MapInstance mapInstance;
	/**
	 * 包括当前区块在内，以及周围的区块一共9个区块
	 */
	private List<MapRegion> neighbours = new ArrayList<MapRegion>(9);
	/**
	 * 区块中的对象
	 */
	private ConcurrentHashMap<Long, MapObject> objects = new ConcurrentHashMap<Long, MapObject>();

	MapRegion(String id, MapInstance mapInstance) {
		this.regionId = id;
		this.mapInstance = mapInstance;
		this.neighbours.add(this);
		this.broadcastGroupName = "MapRegion_" + id;
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
	 */
	public MapInstance getMapInstance() {
		return mapInstance;
	}

	/**
	 * 添加邻居区块
	 * @param neighbour
	 */
	void addNeighbourRegion(MapRegion neighbour) {
		neighbours.add(neighbour);
	}
	
	/**
	 * 发生了区域切换，加入新区块
	 */
	void add(MapObject newObject) {
		if(this.objects.putIfAbsent(newObject.getObjectId(), newObject) == null) {
			MapRegion oldRegion = newObject.getPosition().getMapRegion();
			if(oldRegion != null) {
				oldRegion.objects.remove(newObject.getObjectId());
				//新进对象和它旧视野里已经看不见的对象之间互相通知notSee
				Set<MapRegion> oldCopy = new HashSet<>(oldRegion.neighbours);
				oldCopy.removeAll(this.neighbours);
				for (MapRegion r : oldCopy) {
					for (MapObject o : r.objects.values()) {
						if(o instanceof AbstractSpirit)
							((AbstractSpirit)o).notSee(newObject);
						if(newObject instanceof AbstractSpirit)
							((AbstractSpirit)newObject).notSee(o);
//						if(newObject instanceof Player)
//							Services.tcpService.leaveGroup(r.broadcastGroupName, ((Player)newObject).channelId);
					}
				}
			}
			
			//新进对象和它新视野里新看见的对象之间互相通知see
			Set<MapRegion> newCopy = new HashSet<>(this.neighbours);
			if(oldRegion != null)
				newCopy.removeAll(oldRegion.neighbours);
			for (MapRegion r : newCopy) {
				for (MapObject o : r.objects.values()) {
					if(o != newObject) {
						if(o instanceof AbstractSpirit)
							((AbstractSpirit)o).see(newObject);
						if(newObject instanceof AbstractSpirit)
							((AbstractSpirit)newObject).see(o);
					}
				}
//				if(newObject instanceof Player)
//					Services.tcpService.joinGroup(r.broadcastGroupName, ((Player)newObject).channelId);
			}
		}
	}
	
	void remove(MapObject object) {
		if(this.objects.remove(object.getObjectId()) != null) {
			for (MapRegion r : this.neighbours) {
				for (MapObject o : r.objects.values()) {
					if(o instanceof AbstractSpirit)
						((AbstractSpirit)o).notSee(object);
					if(object instanceof AbstractSpirit)
						((AbstractSpirit)object).notSee(o);
				}
			}
//			if(object instanceof Player) {
//				for (MapRegion r : this.neighbours)
//					Services.tcpService.leaveGroup(r.broadcastGroupName, ((Player)object).channelId);
//			}
		}
	}
	
	public void broadcast(Message message) {
		for (MapRegion r : this.neighbours) {
			for (MapObject o : r.objects.values()) {
				if(o instanceof Player) {
					((Player)o).send(message);
				}
			}
		}
	}
}
