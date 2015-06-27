package com.shadowgame.rpg.modules.item;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import xgame.core.cache.AbstractCacheObject;
import xgame.core.cache.CacheObject;
import xgame.core.util.JsonUtils;

import com.alibaba.fastjson.JSONArray;
import com.shadowgame.rpg.core.AppException;
import com.shadowgame.rpg.persist.dao.TPlayerKnapsackDao;
import com.shadowgame.rpg.persist.entity.TPlayerKnapsack;
import com.shadowgame.rpg.service.Services;

/**
 * @author wcj10051891@gmail.com
 */
public class Knapsack extends AbstractCacheObject<Long, TPlayerKnapsack> {
	
	public static final int DEFAULT_CAPACITY = 100;
	private static final TPlayerKnapsackDao dao = Services.daoFactory.get(TPlayerKnapsackDao.class);
	public TPlayerKnapsack entity;
	public Long[] items;

	/**
	 * @param key
	 * @return
	 */
	@Override
	public TPlayerKnapsack get(Long key) {
		return dao.get(key);
	}
	
	/**
	 * 创建背包，需要参数contextParam[playerId]
	 * @param contextParam
	 * @return
	 */
	@Override
	public TPlayerKnapsack create(Object... contextParam) {
		TPlayerKnapsack entity = new TPlayerKnapsack();
		entity.id = (Long)contextParam[0];
		entity.items = "[]";
		entity.capacity = DEFAULT_CAPACITY;
		dao.insert(entity);
		return entity;
	}

	/**
	 * 初始化背包
	 * @param entity
	 * @param contextParam
	 * @return
	 */
	@Override
	public CacheObject<Long, TPlayerKnapsack> init(TPlayerKnapsack entity, Object... contextParam) {
		this.entity = entity;
		this.items = new Long[entity.capacity];
		JSONArray data = JsonUtils.parseArray(this.entity.items);
		int size = data.size();
		for(int i = 0; i < size && i < entity.capacity; i++) {
			Object object = data.get(i);
			if(object != null && object instanceof Long) {
				Long playerItemId = (Long)object;
				if(playerItemId > 0) {
					PlayerItem playerItem = Services.cacheService.get(playerItemId, PlayerItem.class, true);
					if(playerItem != null)
						items[i] = playerItemId;
				}
			}
		}
		return this;
	}
	
	public boolean remain(int n) {
		int remain = 0;
		for(int i = 0; i < items.length; i++) {
			if(items[i] == null)
				++remain;
			if(remain >= n)
				return true;
		}
		return false;
	}
	
	public int remain() {
		int remain = 0;
		for(int i = 0; i < items.length; i++) {
			if(items[i] == null)
				++remain;
		}
		return remain;
	}
	
	public boolean canPut(List<PlayerItem> playerItems) {
		return remain(playerItems.size());
	}
	
	public void put(List<PlayerItem> playerItems) throws Exception {
		if(!canPut(playerItems))
			throw new AppException("包裹容量不足");
		Iterator<PlayerItem> it = new HashSet<PlayerItem>(playerItems).iterator();
		for(int i = 0; i < items.length && it.hasNext(); i++) {
			if(items[i] == null) {
				items[i] = it.next().getKey();
				it.remove();
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void update() {
		entity.items = JsonUtils.toJson(items);
		dao.update(entity);
	}

	/**
	 * @return
	 */
	@Override
	public Long getKey() {
		return this.entity.id;
	}
}