package com.shadowgame.rpg.modules.mission;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import xgame.core.cache.AbstractCacheObject;
import xgame.core.cache.CacheObject;
import xgame.core.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shadowgame.rpg.core.NoticeException;
import com.shadowgame.rpg.data.MissionData;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.persist.dao.TPlayerMissionDao;
import com.shadowgame.rpg.persist.entity.TPlayerMission;
import com.shadowgame.rpg.service.Services;

public class PlayerMissionManager extends AbstractCacheObject<Long, TPlayerMission>  {
	private TPlayerMissionDao dao = Services.daoFactory.get(TPlayerMissionDao.class);
	private TPlayerMission entity;
    private Set<Integer> finishedMissions = new HashSet<Integer>();
    private Map<Integer, com.shadowgame.rpg.modules.mission.PlayerMission> acceptedMissions = new ConcurrentHashMap<Integer, com.shadowgame.rpg.modules.mission.PlayerMission>();
    private AtomicInteger serialId = new AtomicInteger(Long.valueOf(System.currentTimeMillis() / 1000l).intValue());
    private Player player;
	
	/**
	 * 初始化，需要参数contextParam[player]
	 * @param entity
	 * @param contextParam
	 * @return
	 */
	@Override
	public CacheObject<Long, TPlayerMission> init(TPlayerMission entity, Object... contextParam) {
		this.entity = entity;
		this.player = (Player) contextParam[0];
		if(StringUtils.hasText(entity.finishMission))
			this.finishedMissions.addAll(JSONArray.parseArray(entity.finishMission, Integer.class));
		if(StringUtils.hasText(entity.acceptMission))
			for(JSONObject json : JSONArray.parseArray(entity.finishMission, JSONObject.class)) {
				com.shadowgame.rpg.modules.mission.PlayerMission pm = new com.shadowgame.rpg.modules.mission.PlayerMission(player, json);
				pm.active();
				this.acceptedMissions.put(pm.mission.entity.id, pm);
			}
		return this;
	}

	@Override
	public TPlayerMission get(Long key) {
		return dao.get(key);
	}

	/**
	 * 创建缓存对象，需要参数contextParam[player]
	 * @param contextParam
	 * @return
	 */
	@Override
	public TPlayerMission create(Object... contextParam) {
		this.player = (Player)contextParam[0];
		TPlayerMission entity = new TPlayerMission();
		entity.id = this.player.getKey();
		entity.acceptMission = "[]";
		entity.finishMission = "[]";
		dao.insert(entity);
		return entity;
	}

	@Override
	public void update() {
		dao.update(this.entity);
	}

	@Override
	public Long getKey() {
		return this.entity.id;
	}
	
	public void accept(Integer missionId) {
        if (this.acceptedMissions.containsKey(missionId))
            throw new NoticeException("已经接受该任务了");
        Mission mission = Services.data.get(MissionData.class).missions.get(missionId);
        if (mission == null)
            throw new NoticeException("任务不存在");
        //1、检测是否可以接受该任务
        //2、处理接受任务时候执行的一些逻辑
        com.shadowgame.rpg.modules.mission.PlayerMission pm = 
    		new com.shadowgame.rpg.modules.mission.PlayerMission(player, this.serialId.addAndGet(1), mission);
        pm.active();
        acceptedMissions.put(missionId, pm);
    }
	
	public void giveUp(Integer missionId) {
		com.shadowgame.rpg.modules.mission.PlayerMission pm = this.acceptedMissions.get(missionId);
        if (pm == null)
            throw new NoticeException("要放弃的任务不存在");
        pm.passivate();
        /*
         * 执行放弃任务时候的一些逻辑
         */
        this.acceptedMissions.remove(missionId);
	}
	
	public void finish(Integer missionId) {
		com.shadowgame.rpg.modules.mission.PlayerMission pm = this.acceptedMissions.get(missionId);
        if (pm != null) {
	        if (!pm.checkFinish(false))
	            throw new NoticeException("任务未完成");
	        pm.passivate();
	        this.acceptedMissions.remove(missionId);
	        /*
	         * 给奖励
	         */
        }
	}
}
