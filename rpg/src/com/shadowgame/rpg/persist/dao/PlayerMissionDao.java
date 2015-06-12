package com.shadowgame.rpg.persist.dao;
import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;

import com.shadowgame.rpg.persist.entity.PlayerMission;
@Dao
public interface PlayerMissionDao {
	@Sql(value="insert into player_mission(	`id`,	`acceptMission`,	`finishMission`	) values(	:playerMission.id,	:playerMission.acceptMission,	:playerMission.finishMission	)")
	void insert(@Arg(value="playerMission") PlayerMission o);
	
	@Sql(value="update player_mission set 	`id`=:playerMission.id,	`acceptMission`=:playerMission.acceptMission,	`finishMission`=:playerMission.finishMission	 where id=:playerMission.id")
	void update(@Arg(value="playerMission") PlayerMission o);

	@Sql(value="select * from player_mission where id=:id")
	PlayerMission get(@Arg(value="id") Long id);
}

