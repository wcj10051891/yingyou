package com.shadowgame.rpg.modules.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import xgame.core.cache.AbstractCacheObject;
import xgame.core.util.CountMap;

import com.shadowgame.rpg.persist.dao.ItemDao;
import com.shadowgame.rpg.service.Services;

/**
 * @author wcj10051891@gmail.com
 */
public class Item extends AbstractCacheObject<Integer, com.shadowgame.rpg.persist.entity.Item> {
	
	private static final ItemDao dao = Services.daoFactory.get(ItemDao.class);
	public com.shadowgame.rpg.persist.entity.Item entity;

	@Override
	public com.shadowgame.rpg.persist.entity.Item get(Integer key) {
		return dao.get(key);
	}
	
	@Override
	public Item init(com.shadowgame.rpg.persist.entity.Item entity, Object... contextParam) {
		this.entity = entity;
		return this;
	}

	/**
	 * @return
	 */
	@Override
	public Integer getKey() {
		return this.entity.id;
	}
	
	public List<PlayerItem> createPlayerItem(int num, Long playerId) {
		List<PlayerItem> result = new ArrayList<PlayerItem>(num);
		int n = num / entity.maxStack;
		int mod = num % entity.maxStack;
		if(mod > 0)
			n++;
		for(int i = 0; i < n; i++) {
			int pNum = 1;
			if(mod > 0 && i == n - 1)
				pNum = mod;
			else
				pNum = entity.maxStack;
			result.add(Services.cacheService.create(PlayerItem.class, new Object[]{entity.id, playerId, pNum, entity.bindType}));
		}
		return result;
	}
	
	/**
	 * @param keys
	 * @return
	 */
	@Override
	public Map<Integer, com.shadowgame.rpg.persist.entity.Item> gets(List<Integer> keys) {
		Map<Integer, com.shadowgame.rpg.persist.entity.Item> result = new HashMap<Integer, com.shadowgame.rpg.persist.entity.Item>(keys.size());
		for(com.shadowgame.rpg.persist.entity.Item item : dao.getByItemIds(keys))
			result.put(item.getId(), item);
		return result;
	}
	
	public static List<PlayerItem> createPlayerItem(CountMap<Integer, Integer> itemIdAndNums, Long playerId) {
		Map<Integer, Item> items = Services.cacheService.gets(itemIdAndNums.keySet(), Item.class, true);
		List<PlayerItem> result = new ArrayList<PlayerItem>(itemIdAndNums.size());
		for (Entry<Integer, Integer> idAndNum : itemIdAndNums.entrySet())
			result.addAll(items.get(idAndNum.getKey()).createPlayerItem(idAndNum.getValue(), playerId));
		return result;
	}
}
