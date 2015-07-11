package com.shadowgame.rpg.modules.core;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.cache.CacheObject;
import xgame.core.event.EventDispatcher;
import xgame.core.util.CountMap;
import xgame.core.util.JsonUtils;
import xgame.core.util.ProcessQueue;
import xgame.core.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.shadowgame.rpg.modules.buff.PlayerBuffList;
import com.shadowgame.rpg.modules.common.DailyAttribute;
import com.shadowgame.rpg.modules.cooldown.Cooldown;
import com.shadowgame.rpg.modules.item.Item;
import com.shadowgame.rpg.modules.item.Knapsack;
import com.shadowgame.rpg.modules.map.MapInstance;
import com.shadowgame.rpg.modules.map.World;
import com.shadowgame.rpg.modules.mission.PlayerMissionManager;
import com.shadowgame.rpg.modules.skill.PlayerSkillList;
import com.shadowgame.rpg.msg.login_11.Sc_11002;
import com.shadowgame.rpg.msg.map_12.Sc_12002;
import com.shadowgame.rpg.msg.map_12.Sc_12003;
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
	public ProcessQueue processQueue;
	public EventDispatcher eventDispatcher;
	public PlayerMissionManager missionManager;
	public Cooldown cooldown;
	private boolean isOnline;

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
		this.objectId = entity.id;
		this.entity = entity;
		this.daily = new DailyAttribute(!StringUtils.hasText(this.entity.daily) ? "{}" : this.entity.daily);
		this.extAttribute = JsonUtils.parseObject(!StringUtils.hasText(this.entity.extAttribute) ? "{}" : this.entity.extAttribute);
		return this;
	}
	
	public void onLogin(Channel channel) {
		this.isOnline = true;
		this.channel = channel;
		this.channel.setAttachment(this);

		Services.app.world.allPlayers.add(this);
		Services.tcpService.joinGroup(World.GROUP_NAME, channel);
		
		this.processQueue = new ProcessQueue(Services.threadService.threadPool);
		this.eventDispatcher = new EventDispatcher();
		
		this.knapsack = Services.cacheService.get(this.entity.id, Knapsack.class, true);
		if(this.knapsack == null)
			this.knapsack = Services.cacheService.create(Knapsack.class, this.entity.id);
		
		this.missionManager = Services.cacheService.get(this.entity.id, PlayerMissionManager.class, true, this);
		if(missionManager == null)
			missionManager = Services.cacheService.create(PlayerMissionManager.class, this);

		this.skillList = new PlayerSkillList(this);
		this.buffList = new PlayerBuffList(this);
		this.cooldown = new Cooldown();
		//init attrs
		this.attrs = new FighterAttrs(this);
		this.attrs.initAttr(AttrType.hp, entity.hp);
		this.attrs.initAttr(AttrType.maxHp, 100);
		this.attrs.initAttr(AttrType.mp, entity.mp);
		this.attrs.initAttr(AttrType.maxMp, 100);
		this.attrs.initAttr(AttrType.atk, 10);
		this.attrs.initAttr(AttrType.def, 5);
		this.attrs.initAttr(AttrType.damageFactor1, 1);
		this.attrs.initAttr(AttrType.damageFactor2, 1);
		
		//返回上次场景
		MapInstance lastMap = null;
		if(entity.lastInstanceId != null && entity.lastInstanceId > 0)
			lastMap = Services.app.world.mapInstances.get(entity.lastInstanceId);
		if(lastMap == null && entity.lastMapId != null && entity.lastMapId > 0)
			lastMap = Services.app.world.getWorldMap(entity.lastMapId).getDefaultInstance();
		if(lastMap == null)
			lastMap = Services.app.world.getWorldMap(1).getDefaultInstance();
		int lastMapX = 0;
		if(entity.lastMapX != null && entity.lastMapX > 0)
			lastMapX = entity.lastMapX;
		int lastMapY = 0;
		if(entity.lastMapY != null && entity.lastMapY > 0)
			lastMapY = entity.lastMapY;
		Services.app.world.updatePosition(this, lastMap, lastMapX, lastMapY);
		
		send(new Sc_11002().from(lastMap.getId()));
	}
	
	public void onLogout() {
		this.isOnline = false;
		Services.app.world.allPlayers.remove(this);
		Services.tcpService.leaveGroup(World.GROUP_NAME, channel);
		try {
			//保存离线场景
			MapInstance instance = this.position.getMapInstance();
			instance.remove(this);
			entity.lastInstanceId = instance.getId();
			entity.lastMapId = instance.getGameMap().getKey();
			entity.lastMapX = this.position.getX();
			entity.lastMapY = this.position.getY();
			this.position = null;
		} catch (Exception e) {
			log.error("player {} not in map", this.getKey());
		}
		
		Services.cacheService.saveAsync(this);
		Services.cacheService.saveAsync(this.knapsack);
		Services.cacheService.saveAsync(this.missionManager);
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
		if(isOnline)
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
	 * 玩家创建，需要参数contextParam[username, nickname,vocation]
	 * @param contextParam
	 * @return
	 */
	@Override
	public TPlayer create(Object... contextParam) {
		TPlayer p = createEntity((String)contextParam[0], (String)contextParam[1], (Integer)contextParam[2]);
		dao.insert(p);
		return p;
	}
	
	public static TPlayer createEntity(String username, String nickname, int vocation) {
		TPlayer p = new TPlayer();
		p.id = UniqueId.next();
		p.createTime = new Timestamp(System.currentTimeMillis());
		p.daily = "{}";
		p.lv = 1;
		p.exp = 0;
		p.extAttribute = "{}";
		p.skill = "[]";
		p.buff = "[]";
		p.username = username;
		p.nickname = nickname;
		p.vocation = vocation;
		return p;
	}

	/**
	 * 
	 */
	@Override
	public void update() {
		this.entity.daily = this.daily.toString();
		this.entity.extAttribute = this.extAttribute.toString();
		this.entity.skill = this.skillList.toString();
		this.entity.buff = this.buffList.toString();
		dao.update(entity);
	}
	
	@Override
	public void see(List<MapObject> objects) {
		if(isOnline) {
			send(new Sc_12002().from(objects));
			log.debug("player {} see object {}", this, objects);
		}
	}
	
	@Override
	public void notSee(List<MapObject> objects) {
		if(isOnline) {
			send(new Sc_12003().from(objects));
			log.debug("player {} not see object {}", this, objects);
		}
	}
	
	public boolean isOnline() {
		return this.isOnline;
	}
	
	@Override
	public void onEnterMap(MapInstance map) {
//		send(new Sc_12001().from(map));
	}
}
