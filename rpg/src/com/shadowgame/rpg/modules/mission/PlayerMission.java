package com.shadowgame.rpg.modules.mission;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shadowgame.rpg.data.MissionData;
import com.shadowgame.rpg.data.MissionGoalData;
import com.shadowgame.rpg.modules.core.Player;
import com.shadowgame.rpg.service.Services;

public class PlayerMission {
	private int id;
	public Mission mission;
	private List<MissionGoal<?>> goals;
	private int status = MissionStatus.NEW.getValue();
    protected Timestamp acceptTime;
    private Player player;
	
	public PlayerMission(Player player, int id, Mission mission) {
		this.player = player;
        this.mission = mission;
        this.id = id;
        this.goals = new ArrayList<MissionGoal<?>>();
        this.acceptTime = new Timestamp(System.currentTimeMillis());
        if (mission.goals == null || mission.goals.isEmpty()) {// 没有条件，立即完成
            this.status = MissionStatus.CAN_FINISH.getValue();
        } else {
            this.status = MissionStatus.ACCEPTED.getValue();
            for (JSONObject goal : mission.goals)
                goals.add(Services.data.get(MissionGoalData.class).createMissionGoal(goal, goal.getJSONObject("param")));
        }
    }
	

	public PlayerMission(Player player, JSONObject data) {
		this.player = player;
		fromJson(data);
	}

	public boolean checkFinish() {
		return checkFinish(true);
	}

	/**
	 * 是否完成
	 * @param save 是否保存
	 * @return
	 */
	public boolean checkFinish(boolean save) {
		for (MissionGoal<?> goal : goals) {
			if (!goal.isFinish()) {
				if (this.status != MissionStatus.ACCEPTED.getValue()) {
					this.status = MissionStatus.ACCEPTED.getValue();
					statusChanged(save);
				}
				return false;
			}
		}
		if (this.status != MissionStatus.CAN_FINISH.getValue()) {
			this.status = MissionStatus.CAN_FINISH.getValue();
			statusChanged(save);
		}
		return true;
	}

	private void statusChanged(boolean save) {
//		if (save)
//			save();
//		this.player.fireEvent(new PlayerMissionStateChangeEvent(this));
	}

	public Player getPlayer() {
		return player;
	}
	
	/**
     * 激活
     */
    public void active()
    {
        for (MissionGoal<?> goal : goals)
            goal.onActivated(this);
    }

    /**
     * 清理
     */
    public void passivate()
    {
        for (MissionGoal<?> goal : goals)
            goal.onPassivated(this);
    }
	
	public void fromJson(JSONObject persistData) {
		this.id = persistData.getIntValue("id");
        this.status = persistData.getIntValue("status");
        Long acceptTime = (Long) persistData.getLong("acceptTime");
        if (acceptTime != null)
            this.acceptTime = new Timestamp(acceptTime);
        this.mission = Services.data.get(MissionData.class).missions.get(persistData.getIntValue("missionId"));
        JSONArray array = persistData.getJSONArray("goals");
        for (int i = 0; i < array.size(); i++) {
        	JSONObject goalJson = (JSONObject)JSONObject.toJSON(array.get(i));
        	JSONObject param = this.mission.goals.get(i);
        	this.goals.add(Services.data.get(MissionGoalData.class).createMissionGoal(goalJson, param));
		}
	}
	
	public JSONObject toJson() {
		JSONObject d = new JSONObject();
		d.put("id", this.id);
		d.put("status", this.status);
		d.put("acceptTime", this.acceptTime);
		d.put("missionId", this.mission.entity.id);
		JSONArray a = new JSONArray();
		for (MissionGoal<?> g : this.goals)
			a.add(g.toJson());
		d.put("goals", a);
		return d;
	}
	
	public enum MissionStatus
    {
        NEW("新建", 0), 
        CAN_ACCEPT("可接", 1), 
        ACCEPTED("已接未完成", 2), 
        CAN_FINISH("可以交", 3), 
        CAN_NOT_ACCEPT("暂时还不能接，但是可以看见的任务", 4);

        private String name;

        private int value;

        private MissionStatus(String name, int value)
        {
            this.name = name;
            this.value = value;
        }

        public String getName()
        {
            return name;
        }

        public int getValue()
        {
            return value;
        }
    }
}
