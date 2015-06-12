package com.shadowgame.rpg.modules.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.cache.CacheObject;
import xgame.core.event.BaseObservable;
import xgame.core.net.server.tcp.Groups;
import xgame.core.util.CountMap;
import xgame.core.util.JsonUtils;
import xgame.core.util.ProcessQueue;
import xgame.core.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.shadowgame.rpg.modules.common.DailyAttribute;
import com.shadowgame.rpg.modules.item.Item;
import com.shadowgame.rpg.modules.item.Knapsack;
import com.shadowgame.rpg.modules.item.PlayerItem;
import com.shadowgame.rpg.modules.map.Position;
import com.shadowgame.rpg.modules.mission.PlayerMissionManager;
import com.shadowgame.rpg.persist.dao.PlayerDao;
import com.shadowgame.rpg.service.Services;

public class Player extends AbstractFighter implements CacheObject<Long, com.shadowgame.rpg.persist.entity.Player> {
	private static final PlayerDao dao = Services.daoFactory.get(PlayerDao.class);
	private static final Logger log = LoggerFactory.getLogger(Player.class);
	public com.shadowgame.rpg.persist.entity.Player entity;
	
	public Channel channel;
	public DailyAttribute daily;
	public JSONObject extAttribute;
	public Knapsack knapsack;
	public ProcessQueue processQueue = new ProcessQueue(Services.threadService.threadPool);
	public BaseObservable eventManager;
	public PlayerMissionManager missionManager;

	/**
	 * @param key
	 * @return
	 */
	@Override
	public com.shadowgame.rpg.persist.entity.Player get(Long key) {
		return dao.get(key);
	}
	
	public Player init(com.shadowgame.rpg.persist.entity.Player entity, Object attachment) {
		this.objectId = entity.id;
		this.entity = entity;
		this.daily = new DailyAttribute(!StringUtils.hasText(this.entity.daily) ? "{}" : this.entity.daily);
		this.extAttribute = JsonUtils.parseObject(!StringUtils.hasText(this.entity.extAttribute) ? "{}" : this.entity.extAttribute);
		this.knapsack = Services.cacheService.get(this.objectId, Knapsack.class, true, null);
		return this;
	}
	
	public void onLogin() {
		Services.appService.world.allPlayers.add(this);
		Services.tcpService.joinGroup(Groups.World, channel);
		
		missionManager = Services.cacheService.get(this.entity.id, PlayerMissionManager.class, true, this);
		if(missionManager == null) {
			missionManager = Services.cacheService.create(this.getObjectId(), PlayerMissionManager.class, this);
		}
	}
	
	public void onLogout() {
		Services.appService.world.allPlayers.remove(this);
		Services.tcpService.leaveGroup(Groups.World, channel);
		this.getPosition().getMapRegion().remove(this);
		this.channel = null;
		
		Services.cacheService.saveAsync(this);
//		Services.cacheService.saveAsync(this.knapsack);
	}

	@Override
	public void delete() {
	}

	@Override
	public Long getKey() {
		return this.objectId;
	}

	@Override
	public String toString() {
		return String.valueOf(this.objectId);
	}
	
	public void send(Object message) {
		Services.tcpService.send(message, this.channel);
	}
	
	public void disconnect(boolean synchronize) {
		ChannelFuture future = Services.tcpService.disconnect(this.channel);
		if(synchronize)
			try {
				future.await();
			} catch (Exception e) {
				log.error("disconnect player error.", e);
			}
	}
	
	public void give(CountMap<Integer, Integer> itemIdAndNums) throws Exception {
		List<PlayerItem> items = Item.createPlayerItem(itemIdAndNums, this.objectId);
		this.knapsack.put(items);
		List<com.shadowgame.rpg.persist.entity.PlayerItem> playerItemEntitys = new ArrayList<com.shadowgame.rpg.persist.entity.PlayerItem>(items.size());
		for (PlayerItem playerItem : items)
			playerItemEntitys.add(playerItem.entity);
		PlayerItem.dao.insertBatch(playerItemEntitys);
	}

	/**
	 * @param keys
	 * @return
	 */
	@Override
	public Map<Long, CacheObject<Long, com.shadowgame.rpg.persist.entity.Player>> gets(
			List<Long> keys) {
		return null;
	}

	/**
	 * 
	 */
	@Override
	public com.shadowgame.rpg.persist.entity.Player create(Object attachment) {
		dao.insert(entity);
		return null;
	}

	/**
	 * 
	 */
	@Override
	public void update() {
		this.entity.daily = this.daily.toString();
		this.entity.extAttribute = this.extAttribute.toString();
		dao.update(entity);
	}

	@Override
	public Long getObjectId() {
		return this.objectId;
	}

	@Override
	public Position getPosition() {
		return this.position;
	}
}
