package com.shadowgame.rpg.modules.mission;

import xgame.core.event.Event;
import xgame.core.event.EventListener;

import com.alibaba.fastjson.JSONObject;

/**
 * 任务目标，有物品收集，杀怪，做某些操作，
 * @author wcj10051891@gmail.com
 * @date 2015年6月9日 下午3:13:26
 */
public abstract class MissionGoal<E extends Event> {
	/**
	 * 目标唯一键，英文简称
	 */
	private String key;
	/**
	 * 目标参数
	 */
	private JSONObject param;
	/**
	 * 目标当前完成次数
	 */
    protected int current;
    /**
     * 任务监听器
     */
    private EventListener<E> monitor;

	public MissionGoal(String key, JSONObject param, int current) {
		this.key = key;
		this.param = param;
		this.current = current;
	}
	
	public boolean isFinish() {
		return this.current >= getAll();
	}
	
	public void onActivated(PlayerMission pm) {
		this.monitor = createEventListener(pm);
		pm.getPlayer().eventDispatcher.addListener(this.monitor);
	}

	protected abstract EventListener<E> createEventListener(PlayerMission pm);

	public void onPassivated(PlayerMission pm) {
		if(this.monitor != null) {
			pm.getPlayer().eventDispatcher.removeListener(this.monitor);
			this.monitor = null;
		}
	}
	
	public boolean onUpdate(PlayerMission playerMission, int count) {
		this.current = count;
		int all = getAll();
		if(this.current > all)
			this.current = all;
        playerMission.checkFinish();
//        playerMission.getPlayer().send(playerMission.getMission().toDto(playerMission), playerMission.getPlayer());
        boolean isFinish = isFinish();
        if (isFinish)
        	finish(playerMission);
        return isFinish;
	}
	
	protected JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("key", this.key);
		json.put("current", this.current);
		return json;
	}
	
	private int getAll() {
		return this.param.getIntValue("count");
	}
	
	@Override
	public String toString() {
		return toJson().toJSONString();
	}
	
	public void finish(PlayerMission playerMission)
    {
        this.current = this.getAll();
//        if(this.finishDo != null)
//    		this.finishDo.run(playerMission.getPlayer(), runtimeParam);
    	onPassivated(playerMission);
    }
}
