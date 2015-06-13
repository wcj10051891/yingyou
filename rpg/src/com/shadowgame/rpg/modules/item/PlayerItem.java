package com.shadowgame.rpg.modules.item;

import java.sql.Timestamp;

import xgame.core.cache.AbstractCacheObject;
import xgame.core.cache.CacheObject;

import com.shadowgame.rpg.persist.dao.PlayerItemDao;
import com.shadowgame.rpg.service.Services;
import com.shadowgame.rpg.util.UniqueId;

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
	 * 
	 * @param entity
	 * @param contextParam
	 * @return
	 */
	@Override
	public CacheObject<Long, com.shadowgame.rpg.persist.entity.PlayerItem> init(com.shadowgame.rpg.persist.entity.PlayerItem entity, Object... contextParam) {
		this.entity = entity;
		this.item = Services.cacheService.get(this.entity.itemId, Item.class, true);
		return this;
	}
	
	/**
	 * 创建玩家道具，需要参数contextParam[itemId，playerId，堆叠数，是否绑定]
	 * @return
	 */
	@Override
	public com.shadowgame.rpg.persist.entity.PlayerItem create(Object... contextParam) {
		com.shadowgame.rpg.persist.entity.PlayerItem pmEntity = new com.shadowgame.rpg.persist.entity.PlayerItem();
		pmEntity.id = UniqueId.next();
		pmEntity.createTime = new Timestamp(System.currentTimeMillis());
		pmEntity.itemId = (Integer)contextParam[0];
		pmEntity.playerId = (Long)contextParam[1];
		pmEntity.num = (Integer)contextParam[2];
		if((Integer)contextParam[3] == 1)
			pmEntity.binding = true;
		pmEntity.strengthenLv = 0;
		dao.insert(entity);
		return pmEntity;
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
