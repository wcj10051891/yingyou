package com.shadowgame.rpg.modules.buff;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import xgame.core.util.CommonUtils;

import com.alibaba.fastjson.JSONObject;
import com.shadowgame.rpg.data.BuffData;
import com.shadowgame.rpg.modules.core.AbstractFighter;
import com.shadowgame.rpg.modules.skill.FighterSkill;
import com.shadowgame.rpg.persist.entity.TSkill;
import com.shadowgame.rpg.service.Services;

public class BuffTask {
	/**
	 * 触发buff者
	 */
	public AbstractFighter source;
	/**
	 * 触发buff的技能
	 */
	public FighterSkill sourceSkill;
	/**
	 * buff目标
	 */
	public AbstractFighter target;
	/**
	 * 调度执行目标，buff
	 */
	public BuffLogic buffLogic;
	/**
	 * 已作用次数
	 */
	public int executeCount;
	/**
	 * 作用开始时间
	 */
	public long startTime;
	/**
	 * 调度任务
	 */
	private ScheduledFuture<?> task;

	public BuffTask(AbstractFighter source, FighterSkill sourceSkill, AbstractFighter target, BuffLogic buffLogic) {
		super();
		this.source = source;
		this.sourceSkill = sourceSkill;
		this.target = target;
		this.buffLogic = buffLogic;
	}
	
	public BuffTask(AbstractFighter target, JSONObject json) {
		super();
		this.target = target;
		this.buffLogic = Services.data.get(BuffData.class).buffLogics.get(json.getIntValue("buffId"));
		this.startTime = json.getLongValue("startTime");
		this.executeCount = json.getIntValue("executeCount");
	}
	
	public void start() {
		if(this.startTime == 0) {//首次执行
			buffLogic.onStart(this);
			this.startTime = System.currentTimeMillis();
			long peroid = CommonUtils.nullLongToDef(buffLogic.getPeriod(this));
			if(peroid > 0) {
				this.task = Services.timerService.jdkScheduler.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						run0();
					}
				}, 0, peroid, TimeUnit.MILLISECONDS);
			}
		} else {
			if(!isFinish()) {
				buffLogic.onStart(this);
				long peroid = CommonUtils.nullLongToDef(buffLogic.getPeriod(this));
				if(peroid > 0) {
					this.task = Services.timerService.jdkScheduler.scheduleAtFixedRate(new Runnable() {
						@Override
						public void run() {
							run0();
						}
					}, 0, peroid, TimeUnit.MILLISECONDS);
				}
			}
		}
	}
	
	private void run0() {
		++executeCount;
		buffLogic.onPeriod(this);
		checkFinish();
	}
	
	public void stop() {
		if(task != null) {
			task.cancel(true);
			task = null;
		}
	}
	
	public boolean isFinish() {
		return buffLogic.isFinish(this);
	}
	
	public void checkFinish() {
		if(isFinish()) {
			stop();
			buffLogic.onStop(this);
		}
	}
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("buffId", this.buffLogic.getBuff().id);
		json.put("startTime", this.startTime);
		json.put("executeCount", this.executeCount);
		return json;
	}
}