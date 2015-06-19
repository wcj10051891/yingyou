package com.shadowgame.rpg.modules.core;

import java.sql.Timestamp;
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
import com.shadowgame.rpg.modules.map.Position;
import com.shadowgame.rpg.modules.mission.PlayerMissionManager;
import com.shadowgame.rpg.persist.dao.PlayerDao;
import com.shadowgame.rpg.service.Services;
import com.shadowgame.rpg.util.UniqueId;

public class Player extends AbstractFighter implements CacheObject<Long, com.shadowgame.rpg.persist.entity.Player> {
	public static final PlayerDao dao = Services.daoFactory.get(PlayerDao.class);
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

	@Override
	public Player init(com.shadowgame.rpg.persist.entity.Player entity, Object... contextParam) {
		this.objectId = entity.id;
		this.entity = entity;
		this.daily = new DailyAttribute(!StringUtils.hasText(this.entity.daily) ? "{}" : this.entity.daily);
		this.extAttribute = JsonUtils.parseObject(!StringUtils.hasText(this.entity.extAttribute) ? "{}" : this.entity.extAttribute);
		this.knapsack = Services.cacheService.get(this.objectId, Knapsack.class, true);
		if(this.knapsack == null)
			this.knapsack = Services.cacheService.create(Knapsack.class, this.objectId);
		return this;
	}
	
	public void onLogin(Channel channel) {
		this.channel = channel;
		this.channel.setAttachment(this);
		
		Services.app.world.allPlayers.add(this);
		Services.tcpService.joinGroup(Groups.World, channel);
		
		missionManager = Services.cacheService.get(this.entity.id, PlayerMissionManager.class, true, this);
		if(missionManager == null)
			missionManager = Services.cacheService.create(PlayerMissionManager.class, this);
	}
	
	public void onLogout() {
		Services.app.world.allPlayers.remove(this);
		Services.tcpService.leaveGroup(Groups.World, channel);
		try {
			this.getPosition().getMapInstance().remove(this);
		} catch (Exception e) {
			log.error("player {} not in map", this.getObjectId());
		}
		
		Services.cacheService.saveAsync(this);
		Services.cacheService.saveAsync(this.knapsack);
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
		this.knapsack.put(Item.createPlayerItem(itemIdAndNums, this.objectId));
	}

	/**
	 * @param keys
	 * @return
	 */
	@Override
	public Map<Long, com.shadowgame.rpg.persist.entity.Player> gets(List<Long> keys) {
		return null;
	}

	/**
	 * 玩家创建，需要参数contextParam[username, nickname]
	 * @param contextParam
	 * @return
	 */
	@Override
	public com.shadowgame.rpg.persist.entity.Player create(Object... contextParam) {
		com.shadowgame.rpg.persist.entity.Player p = new com.shadowgame.rpg.persist.entity.Player();
		p.id = UniqueId.next();
		p.createTime = new Timestamp(System.currentTimeMillis());
		p.daily = "{}";
		p.lv = 1;
		p.exp = 0;
		p.extAttribute = "{}";
		p.username = (String)contextParam[0];
		p.nickname = (String)contextParam[1];
		dao.insert(p);
		return p;
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
