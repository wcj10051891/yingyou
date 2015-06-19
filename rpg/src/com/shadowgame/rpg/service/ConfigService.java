package com.shadowgame.rpg.service;

import xgame.core.util.Service;

import com.shadowgame.rpg.modules.exec.ExecutionConfig;
import com.shadowgame.rpg.modules.map.MapConfig;
import com.shadowgame.rpg.modules.mission.MissionConfig;
import com.shadowgame.rpg.modules.mission.MissionGoalConfig;

public class ConfigService implements Service {
	public ExecutionConfig execConfig;
	public MissionGoalConfig missionGoalConfig;
	public MissionConfig missionConfig;
	public MapConfig mapConfig;

	@Override
	public void start() throws Exception {
		execConfig = new ExecutionConfig();
		missionGoalConfig = new MissionGoalConfig();
		missionConfig = new MissionConfig();
		mapConfig = new MapConfig();
	}

	@Override
	public void stop() throws Exception {
	}
}
