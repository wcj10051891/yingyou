package com.shadowgame.rpg.persist.dao;
import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;

import com.shadowgame.rpg.persist.entity.TPlayerMission;
@Dao
public interface TPlayerMissionDao {
	@Sql(value="insert into t_player_mission(	`id`,	`acceptMission`,	`finishMission`	) values(	:tPlayerMission.id,	:tPlayerMission.acceptMission,	:tPlayerMission.finishMission	)")
	void insert(@Arg(value="tPlayerMission") TPlayerMission o);
	
	@Sql(value="delete from t_player_mission where id=:id")
	void delete(@Arg(value="id")Long id);
	
	@Sql(value="update t_player_mission set 	`id`=:tPlayerMission.id,	`acceptMission`=:tPlayerMission.acceptMission,	`finishMission`=:tPlayerMission.finishMission	 where id=:tPlayerMission.id")
	void update(@Arg(value="tPlayerMission") TPlayerMission o);

	@Sql(value="select * from t_player_mission where id=:id")
	TPlayerMission get(@Arg(value="id") Long id);
}

