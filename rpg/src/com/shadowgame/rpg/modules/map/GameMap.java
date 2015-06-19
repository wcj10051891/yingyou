package com.shadowgame.rpg.modules.map;

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
		if(this.defaultInstance == null)
			this.defaultInstance = createInstance();
		return this;
	}

	@Override
	public Integer getKey() {
		return entity.getId();
	}
}
