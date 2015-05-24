package com.shadowgame.rpg.modules.item;

import xgame.core.cache.AbstractCacheObject;
import xgame.core.cache.CacheObject;

import com.shadowgame.rpg.persist.dao.PlayerItemDao;
import com.shadowgame.rpg.service.Services;

/**
 * @author wcj10051891@gmail.com
 */
public class PlayerItem extends AbstractCacheObject<Long, com.shadowgame.rpg.persist.entity.PlayerItem> {
	
	public static final PlayerItemDao dao = Services.daoFactory.get(PlayerItemDao.class);
	public com.shadowgame.rpg.persist.entity.PlayerItem entity;
	public Item item;

	/**
	 * @param key
	 * @return
	 */
	@Override
	public com.shadowgame.rpg.persist.entity.PlayerItem get(Long key) {
		return dao.get(key);
	}
	
	/**
	 * @param entity
	 * @return
	 */
	@Override
	public CacheObject<Long, com.shadowgame.rpg.persist.entity.PlayerItem> init(
			com.shadowgame.rpg.persist.entity.PlayerItem entity) {
		this.entity = entity;
		this.item = Services.cacheService.get(this.entity.itemId, Item.class, true);
		return this;
	}
	
	/**
	 * 
	 */
	@Override
	public void insert() {
		dao.insert(entity);
	}
	
	/**
	 * 
	 */
	@Override
	public void delete() {
		dao.delete(getKey());
	}
	
	/**
	 * 
	 */
	@Override
	public void update() {
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
