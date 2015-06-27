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
import com.shadowgame.rpg.modules.map.MapInstance;
import com.shadowgame.rpg.modules.mission.PlayerMissionManager;
import com.shadowgame.rpg.msg.map_12.Sc_12001;
import com.shadowgame.rpg.msg.map_12.Sc_12002;
import com.shadowgame.rpg.persist.dao.TPlayerDao;
import com.shadowgame.rpg.persist.entity.TPlayer;
import com.shadowgame.rpg.service.Services;
import com.shadowgame.rpg.util.UniqueId;

public class Player extends AbstractFighter implements CacheObject<Long, TPlayer> {
	public static final TPlayerDao dao = Services.daoFactory.get(TPlayerDao.class);
	private static final Logger log = LoggerFactory.getLogger(Player.class);
	public TPlayer entity;
	
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
	public TPlayer get(Long key) {
		return dao.get(key);
	}

	@Override
	public Player init(TPlayer entity, Object... contextParam) {
		this.entity = entity;
		this.daily = new DailyAttribute(!StringUtils.hasText(this.entity.daily) ? "{}" : this.entity.daily);
		this.extAttribute = JsonUtils.parseObject(!StringUtils.hasText(this.entity.extAttribute) ? "{}" : this.entity.extAttribute);
		this.knapsack = Services.cacheService.get(this.entity.id, Knapsack.class, true);
		if(this.knapsack == null)
			this.knapsack = Services.cacheService.create(Knapsack.class, this.entity.id);
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
		
		//返回上次场景
		MapInstance lastMap = null;
		if(entity.lastInstanceId > 0)
			lastMap = Services.app.world.mapInstances.get(entity.lastInstanceId);
		if(lastMap == null && entity.lastMapId > 0)
			lastMap = Services.app.world.getWorldMap(entity.lastMapId).getDefaultInstance();
		if(lastMap == null)
			lastMap = Services.app.world.getWorldMap(1).getDefaultInstance();
		int lastMapX = 0;
		if(entity.lastMapX != null)
			lastMapX = entity.lastMapX;
		int lastMapY = 0;
		if(entity.lastMapY != null)
			lastMapY = entity.lastMapY;
		Services.app.world.updatePosition(this, lastMap, lastMapX, lastMapY);
	}
	
	public void onLogout() {
		Services.app.world.allPlayers.remove(this);
		Services.tcpService.leaveGroup(Groups.World, channel);
		try {
			this.getPosition().getMapInstance().remove(this);
		} catch (Exception e) {
			log.error("player {} not in map", this.getKey());
		}
		

		//保存离线场景
		MapInstance instance = this.position.getMapInstance();
		entity.lastInstanceId = instance.getId();
		entity.lastMapId = instance.getGameMap().getKey();
		entity.lastMapX = this.position.getX();
		entity.lastMapY = this.position.getY();
		
		
		
		
		Services.cacheService.saveAsync(this);
		Services.cacheService.saveAsync(this.knapsack);
	}
	
	public void disconnect() {
		if(this.channel != null)
			Services.tcpService.disconnect(channel);
	}

	@Override
	public void delete() {
	}

	@Override
	public Long getKey() {
		return this.entity.id;
	}

	@Override
	public String toString() {
		return String.valueOf(this.entity.id);
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
		this.knapsack.put(Item.createPlayerItem(itemIdAndNums, this.entity.id));
	}

	/**
	 * @param keys
	 * @return
	 */
	@Override
	public Map<Long, TPlayer> gets(List<Long> keys) {
		return null;
	}

	/**
	 * 玩家创建，需要参数contextParam[username, nickname]
	 * @param contextParam
	 * @return
	 */
	@Override
	public TPlayer create(Object... contextParam) {
		TPlayer p = new TPlayer();
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
	public void see(MapObject object) {
		if(object instanceof Player)
			send(new Sc_12001().from((Player)object));
		else if(object instanceof Monster)
			send(new Sc_12001().from((Monster)object));
		log.debug("player {} see object {}", this, object);
	}
	
	@Override
	public void notSee(MapObject object) {
		if(object instanceof Player)
			send(new Sc_12002().from((Player)object));
		else if(object instanceof Monster)
			send(new Sc_12002().from((Monster)object));
		log.debug("player {} not see object {}", this, object);
	}
}
