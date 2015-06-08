package com.shadowgame.rpg.modules.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 地图
 * @author wcj10051891@gmail.com
 * @Date 2015年5月25日 下午6:58:41
 */
public class GameMap {
	private int mapId;
	private String name;
	/**
	 * 是否是副本类型
	 */
	private boolean instanceType;
	private World world;
	/**
	 * 地图的所有副本
	 */
	private Map<Integer, MapInstance> instances = new ConcurrentHashMap<Integer, MapInstance>();

	/**
	 * 副本序号 
	 */
	private AtomicInteger seq = new AtomicInteger(1);
	
	public GameMap(World world) {
		this.world = world;
		createInstance();
	}
	

	/**
	 * 获取地图ID
	 * @return map id
	 */
	public int getMapId() {
		return mapId;
	}

	/**
	 * 获取地图名字
	 * @return
	 */
	public String getName() {
		return name;
	}

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
		MapInstance instance = new MapInstance(this, seq.getAndIncrement());
		instances.put(instance.getInstanceId(), instance);
		//设置定时销毁
		return instance;
	}

	public MapInstance getInstance(int instanceId) {
		return instances.get(instanceId);
	}

	public void removeInstance(int instanceId) {
		instances.remove(instanceId);
	}
	
	public MapInstance getDefaultInstance() {
		return instances.get(1);
	}

	public boolean isInstanceType() {
		return instanceType;
	}
}
