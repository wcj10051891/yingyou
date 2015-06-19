package com.shadowgame.rpg.persist.dao;
import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;
import java.util.List;
import com.shadowgame.rpg.persist.entity.TMissionGoal;
@Dao
public interface TMissionGoalDao {
	@Sql(value="insert into t_mission_goal(	`key`,	`clazz`,	`param`	) values(	:tMissionGoal.key,	:tMissionGoal.clazz,	:tMissionGoal.param	)")
	Integer insert(@Arg(value="tMissionGoal") TMissionGoal o);
	
	@Sql(value="delete from t_mission_goal where id=:id")
	void delete(@Arg(value="id")Integer id);
	
	@Sql(value="update t_mission_goal set 	`key`=:tMissionGoal.key,	`clazz`=:tMissionGoal.clazz,	`param`=:tMissionGoal.param	 where id=:tMissionGoal.id")
	void update(@Arg(value="tMissionGoal") TMissionGoal o);

	@Sql(value="select * from t_mission_goal where id=:id")
	TMissionGoal get(@Arg(value="id") Integer id);
	
	@Sql(value="select * from t_mission_goal")
	List<TMissionGoal> getAll();
}

