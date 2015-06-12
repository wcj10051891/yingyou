package com.shadowgame.rpg.modules.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.channel.Channel;

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
	private MapRegionCollections<MapObject> objects = new MapRegionCollections<>();

	@SuppressWarnings("unchecked")
	private static final class MapRegionCollections<V extends MapObject> extends ConcurrentHashMap<Long, V> {
		private static final long serialVersionUID = 1L;
		private Map<Class<V>, Collection<V>> type2Objects = new ConcurrentHashMap<Class<V>, Collection<V>>();
		@Override
		public V put(Long key, V value) {
			V result = super.put(key, value);
			getObjectsByType((Class<V>) value.getClass()).add(value);
			return result;
		}
		
		private Collection<V> getObjectsByType(Class<V> type) {
			Collection<V> set = type2Objects.get(type);
			if(set == null) {
				set = new HashSet<>();
				type2Objects.put(type, set);
			}
			return set;
		}
		
		@Override
		public V putIfAbsent(Long key, V value) {
			V result = super.putIfAbsent(key, value);
			getObjectsByType((Class<V>) value.getClass()).add(value);
			return result;
		}
		
		@Override
		public V remove(Object key) {
			V result = super.remove(key);
			if(result != null)
				getObjectsByType((Class<V>) result.getClass()).remove(result);
			return result;
		}
		
		@Override
		public void clear() {
			super.clear();
			type2Objects.clear();
		}
	}
	
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
			}
		}
	}
	
	public void remove(MapObject object) {
		if(this.objects.remove(object.getObjectId()) != null) {
			for (MapRegion r : this.neighbours) {
				for (MapObject o : r.objects.values()) {
					if(o instanceof AbstractSpirit)
						((AbstractSpirit)o).notSee(object);
					if(object instanceof AbstractSpirit)
						((AbstractSpirit)object).notSee(o);
				}
			}
		}
	}
	
	public void broadcast(Object message, Player... excludePlayers) {
		Collection<Channel> toChannels = new ArrayList<>();
		Set<Channel> excludeChannels = new HashSet<>();
		Collection<Player> excludePs = new HashSet<>(Arrays.asList(excludePlayers));
		for (MapRegion r : this.neighbours) {
			for (Player player : r.getMapObjectByType(Player.class)) {
				if(excludePs.contains(player))
					excludeChannels.add(player.channel);
				else
					toChannels.add(player.channel);
			}
		}
		if(!toChannels.isEmpty()) {
			Services.tcpService.broadcast(message, toChannels, excludeChannels);
		}
	}
	
	/**
	 * 按对象类型查找对象集合
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends MapObject> Collection<T> getMapObjectByType(Class<T> type) {
		return (Set<T>) this.objects.getObjectsByType((Class<MapObject>) type);
	}
	
	/**
	 * 获取可见的所有对象
	 * @return
	 */
	public Collection<MapObject> getMapObjects() {
		return this.objects.values();
	}
}
