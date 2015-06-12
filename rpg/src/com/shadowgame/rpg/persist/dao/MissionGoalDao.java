package com.shadowgame.rpg.persist.dao;
import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;
import java.util.List;
import com.shadowgame.rpg.persist.entity.MissionGoal;
@Dao
public interface MissionGoalDao {
	@Sql(value="insert into mission_goal(	`key`,	`clazz`,	`param`	) values(	:missionGoal.key,	:missionGoal.clazz,	:missionGoal.param	)")
	Integer insert(@Arg(value="missionGoal") MissionGoal o);
	
	@Sql(value="delete from mission_goal where id=:id")
	void delete(@Arg(value="id")Integer id);
	
	@Sql(value="update mission_goal set 	`key`=:missionGoal.key,	`clazz`=:missionGoal.clazz,	`param`=:missionGoal.param	 where id=:missionGoal.id")
	void update(@Arg(value="missionGoal") MissionGoal o);

	@Sql(value="select * from mission_goal where id=:id")
	MissionGoal get(@Arg(value="id") Integer id);
	
	@Sql(value="select * from mission_goal")
	List<MissionGoal> getAll();
}

