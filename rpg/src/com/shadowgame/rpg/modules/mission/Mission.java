package com.shadowgame.rpg.modules.mission;

import java.util.ArrayList;
import java.util.List;

import xgame.core.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.shadowgame.rpg.modules.exec.Execution;
import com.shadowgame.rpg.persist.entity.TMission;

public class Mission {
	/**
	 * 任务配置
	 */
	public TMission entity;
	/**
	 * 接受条件
	 */
	public List<Execution> acceptConditions;
	/**
	 * 任务目标
	 */
	public List<JSONObject> goals;
	
	public Mission(TMission entity) {
		this.entity = entity;
		this.acceptConditions = new ArrayList<>();
		if(StringUtils.hasText(this.entity.acceptCond1))
			this.acceptConditions.add(new Execution(this.entity.acceptCond1));
		if(StringUtils.hasText(this.entity.acceptCond2))
			this.acceptConditions.add(new Execution(this.entity.acceptCond2));
		if(StringUtils.hasText(this.entity.acceptCond3))
			this.acceptConditions.add(new Execution(this.entity.acceptCond3));
		if(StringUtils.hasText(this.entity.acceptCond4))
			this.acceptConditions.add(new Execution(this.entity.acceptCond4));
		if(StringUtils.hasText(this.entity.acceptCond5))
			this.acceptConditions.add(new Execution(this.entity.acceptCond5));
		
		this.goals = new ArrayList<>();
		if(StringUtils.hasText(this.entity.goal1))
			this.goals.add(JSONObject.parseObject(this.entity.goal1));
		if(StringUtils.hasText(this.entity.goal2))
			this.goals.add(JSONObject.parseObject(this.entity.goal2));
		if(StringUtils.hasText(this.entity.goal3))
			this.goals.add(JSONObject.parseObject(this.entity.goal3));
		if(StringUtils.hasText(this.entity.goal4))
			this.goals.add(JSONObject.parseObject(this.entity.goal4));
		if(StringUtils.hasText(this.entity.goal5))
			this.goals.add(JSONObject.parseObject(this.entity.goal5));
	}
}
