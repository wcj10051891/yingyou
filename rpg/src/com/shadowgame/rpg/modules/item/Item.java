package com.shadowgame.rpg.modules.item;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import xgame.core.cache.AbstractCacheObject;
import xgame.core.cache.CacheObject;
import xgame.core.util.CountMap;

import com.shadowgame.rpg.persist.dao.ItemDao;
import com.shadowgame.rpg.service.Services;
import com.shadowgame.rpg.util.UniqueId;

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
	public Item init(com.shadowgame.rpg.persist.entity.Item entity, Object attachment) {
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
			PlayerItem playerItem = new PlayerItem();
			com.shadowgame.rpg.persist.entity.PlayerItem playerItemEntity = new com.shadowgame.rpg.persist.entity.PlayerItem();
			playerItemEntity.createTime = new Timestamp(System.currentTimeMillis());
			playerItemEntity.id = UniqueId.next();
			playerItemEntity.itemId = entity.id;
			playerItemEntity.binding = false;
			playerItemEntity.strengthenLevel = 0;
			if(mod > 0 && i == n - 1)
				playerItemEntity.num = mod;
			else
				playerItemEntity.num = entity.maxStack;
			playerItemEntity.playerId = playerId;
			if(this.entity.bindType == 1)
				playerItemEntity.binding = true;
			playerItem.init(playerItemEntity, null);
			result.add(playerItem);
		}
		return result;
	}
	
	/**
	 * @param keys
	 * @return
	 */
	@Override
	public Map<Integer, CacheObject<Integer, com.shadowgame.rpg.persist.entity.Item>> gets(
			List<Integer> keys) {
		Map<Integer, CacheObject<Integer, com.shadowgame.rpg.persist.entity.Item>> result = new HashMap<Integer, CacheObject<Integer, com.shadowgame.rpg.persist.entity.Item>>(keys.size());
		for(com.shadowgame.rpg.persist.entity.Item item : dao.getByItemIds(keys))
			result.put(item.getId(), new Item().init(item, null));
		
		return result;
	}
	
	public static List<PlayerItem> createPlayerItem(CountMap<Integer, Integer> itemIdAndNums, Long playerId) {
		Map<Integer, Item> items = Services.cacheService.gets(itemIdAndNums.keySet(), Item.class, true, null);
		List<PlayerItem> result = new ArrayList<PlayerItem>(itemIdAndNums.size());
		for (Entry<Integer, Integer> idAndNum : itemIdAndNums.entrySet())
			result.addAll(items.get(idAndNum.getKey()).createPlayerItem(idAndNum.getValue(), playerId));
		return result;
	}
}
