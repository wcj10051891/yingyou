package com.shadowgame.rpg.modules.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.channel.Channel;

import com.shadowgame.rpg.modules.core.AbstractFighter;
import com.shadowgame.rpg.modules.core.AbstractSpirit;
import com.shadowgame.rpg.modules.core.MapObject;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.service.Services;

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
	private MapInstance mapInstance;
	/**
	 * 包括当前区块在内，以及周围的区块一共9个区块
	 */
	private List<MapRegion> neighbours = new ArrayList<MapRegion>(9);
	/**
	 * 区块中的对象
	 */
	private MapObjectCollection<MapObject> objects = new MapObjectCollection<>();

	MapRegion(String id, MapInstance mapInstance) {
		this.regionId = id;
		this.mapInstance = mapInstance;
		this.neighbours.add(this);
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
			//加进新地图
			this.mapInstance.putObject(newObject);

			List<MapObject> newObjectSelf = Arrays.asList(newObject);
			MapRegion oldRegion = newObject.getPosition().getMapRegion();
			if(oldRegion != null) {
				//从地图中移除
				oldRegion.mapInstance.removeObject(newObject.getObjectId());
				
				oldRegion.objects.remove(newObject.getObjectId());
				//新进对象和它旧视野里已经看不见的对象之间互相通知notSee
				Set<MapRegion> oldCopy = new HashSet<>(oldRegion.neighbours);
				oldCopy.removeAll(this.neighbours);
				List<MapObject> newObjectNotSee = new ArrayList<MapObject>();
				for (MapRegion r : oldCopy) {
					for (MapObject o : r.objects.values()) {
						if(o instanceof AbstractSpirit)
							((AbstractSpirit)o).notSee(newObjectSelf);
						if(newObject instanceof AbstractSpirit)
							newObjectNotSee.add(o);
					}
				}
				if(!newObjectNotSee.isEmpty())
					((AbstractSpirit)newObject).notSee(newObjectNotSee);
			}
			
			//新进对象和它新视野里新看见的对象之间互相通知see
			List<MapObject> newObjectSee = new ArrayList<MapObject>();
			Set<MapRegion> newCopy = new HashSet<>(this.neighbours);
			if(oldRegion != null)
				newCopy.removeAll(oldRegion.neighbours);
			for (MapRegion r : newCopy) {
				for (MapObject o : r.objects.values()) {
					if(o != newObject) {
						if(o instanceof AbstractSpirit)
							((AbstractSpirit)o).see(newObjectSelf);
						if(newObject instanceof AbstractSpirit)
							newObjectSee.add(o);
					}
				}
			}
			if(!newObjectSee.isEmpty())
				((AbstractSpirit)newObject).see(newObjectSee);
		}
	}
	
	/**
	 * 从地图区块中移除，消失了
	 * @param object
	 */
	public void remove(MapObject object) {
		if(this.objects.remove(object.getObjectId()) != null) {
			//从地图中移除
			this.mapInstance.removeObject(object.getObjectId());
			List<MapObject> objectSelf = Arrays.asList(object);
			for (MapRegion r : this.neighbours) {
				for (MapObject o : r.objects.values()) {
					if(o instanceof AbstractSpirit)
						((AbstractSpirit)o).notSee(objectSelf);
				}
			}
		}
	}
	
	public void broadcast(Object message, Player... excludePlayers) {
		Collection<Channel> toChannels = new ArrayList<>();
		Set<Channel> excludeChannels = Collections.emptySet();
		Collection<Player> excludePs = Collections.emptySet();
		if(excludePlayers.length > 0) {
			excludeChannels = new HashSet<>();
			excludePs = new HashSet<>(Arrays.asList(excludePlayers));
		}
		for (Player player : getVisibilityObjectsByType(Player.class)) {
			if(excludePs.contains(player))
				excludeChannels.add(player.channel);
			else if(player.isOnline())
				toChannels.add(player.channel);
		}
		if(!toChannels.isEmpty())
			Services.tcpService.broadcast(message, toChannels, excludeChannels);
	}
	
	/**
	 * 获取当前以及周围8个区块中所有的mapObject
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends MapObject> Collection<T> getVisibilityObjects() {
		Set<T> result = new HashSet<T>();
		for (MapRegion r : this.neighbours) {
			for (MapObject o : r.getMapObjects())
				result.add((T) o);
		}
		return result;
	}
	
	/**
	 * 获取当前以及周围8个区块中所有的指定类型type的mapObject
	 * @param type
	 * @return
	 */
	public <T extends MapObject> Collection<T> getVisibilityObjectsByType(Class<T> type) {
		Set<T> result = new HashSet<T>();
		for (MapRegion r : this.neighbours) {
			Map<Long, T> map = r.getMapObjectByType(type);
			if(!map.isEmpty())
				result.addAll(map.values());
		}
		return result;
	}
	
	/**
	 * 在当前区块中，按对象类型查找对象集合
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends MapObject> Map<Long, T> getMapObjectByType(Class<T> type) {
		return (Map<Long, T>) this.objects.getObjectsByType((Class<MapObject>) type);
	}
	
	/**
	 * 获取当前区块中的所有对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends MapObject> Collection<T> getMapObjects() {
		return (Collection<T>)this.objects.values();
	}
	
	/**
	 * 获取当前区块中所有的战斗对象
	 * @return
	 */
	public Collection<AbstractFighter> getFighters() {
		List<AbstractFighter> result = new ArrayList<AbstractFighter>();
		for (MapObject o : this.objects.values()) {
			if(o instanceof AbstractFighter)
				result.add((AbstractFighter)o);
		}
		return result;
	}
	
	@Override
	public String toString() {
		return mapInstance.toString() + ",region:" + regionId;
	}
}
