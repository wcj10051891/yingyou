package com.shadowgame.rpg.modules.mission.goals;

import xgame.core.event.EventListener;

import com.alibaba.fastjson.JSONObject;
import com.shadowgame.rpg.modules.event.PlayerKillMonsterEvent;
import com.shadowgame.rpg.modules.mission.MissionGoal;
import com.shadowgame.rpg.modules.mission.PlayerMission;

public class KillMonsterMissionGoal extends MissionGoal<PlayerKillMonsterEvent> {

	public KillMonsterMissionGoal(String key, JSONObject param) {
		super(key, param);
	}

	@Override
	protected EventListener<PlayerKillMonsterEvent> createEventListener(final PlayerMission pm) {
		return new EventListener<PlayerKillMonsterEvent>() {
			@Override
			public void onEvent(PlayerKillMonsterEvent event) {
				onUpdate(pm, current + 1);
			}
		};
	}

}
