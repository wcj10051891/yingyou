package com.shadowgame.rpg.modules.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.cache.CacheObject;
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
import com.shadowgame.rpg.persist.dao.PlayerDao;
import com.shadowgame.rpg.service.Services;

public class Player extends Fighter implements CacheObject<Long, com.shadowgame.rpg.persist.entity.Player> {
	private static final PlayerDao dao = Services.daoFactory.get(PlayerDao.class);
	private static final Logger log = LoggerFactory.getLogger(Player.class);
	public com.shadowgame.rpg.persist.entity.Player entity;
	
	public Long id;
	public Integer channelId;
	public DailyAttribute daily;
	public JSONObject extAttribute;
	public Knapsack knapsack;
	public ProcessQueue processQueue = new ProcessQueue(Services.threadService.threadPool);

	/**
	 * @param key
	 * @return
	 */
	@Override
	public com.shadowgame.rpg.persist.entity.Player get(Long key) {
		return dao.get(key);
	}
	
	public Player init(com.shadowgame.rpg.persist.entity.Player entity) {
		this.id = entity.id;
		this.entity = entity;
		this.daily = new DailyAttribute(!StringUtils.hasText(this.entity.daily) ? "{}" : this.entity.daily);
		this.extAttribute = JsonUtils.parseObject(!StringUtils.hasText(this.entity.extAttribute) ? "{}" : this.entity.extAttribute);
		this.knapsack = Services.cacheService.get(this.id, Knapsack.class, true);
		return this;
	}
	
	public void onLogin() {
		Services.appService.players.add(this);
		Services.tcpService.joinGroup(Groups.World, channelId);
	}
	
	public void onLogout() {
		Services.tcpService.channels.remove(channelId);
		Services.appService.players.remove(this);
		Services.tcpService.leaveGroup(Groups.World, channelId);
		channelId = 0;
		
		Services.cacheService.saveAsync(this);
		Services.cacheService.saveAsync(this.knapsack);
	}

	@Override
	public void delete() {
	}

	@Override
	public Long getKey() {
		return id;
	}

	@Override
	public String toString() {
		return String.valueOf(id);
	}
	
	public void send(Object message) {
		Services.tcpService.send(message, channelId);
	}
	

	public void disconnectWaitCompleted() {
		disconnect(false);
	}
	
	public void disconnect(boolean synchronize) {
		ChannelFuture future = Services.tcpService.disconnect(channelId);
		if(synchronize)
			try {
				future.await();
			} catch (Exception e) {
				log.error("disconnect player error.", e);
			}
	}
	
	public void give(CountMap<Integer, Integer> itemIdAndNums) throws Exception {
		List<PlayerItem> items = Item.createPlayerItem(itemIdAndNums, id);
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
	public void insert() {
		dao.insert(entity);
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
}
