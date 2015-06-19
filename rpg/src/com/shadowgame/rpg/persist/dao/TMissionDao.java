package com.shadowgame.rpg.persist.dao;
import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;
import java.util.List;
import com.shadowgame.rpg.persist.entity.TMission;
@Dao
public interface TMissionDao {
	@Sql(value="insert into t_mission(	`id`,	`name`,	`acceptCond1`,	`acceptCond2`,	`acceptCond3`,	`acceptCond4`,	`acceptCond5`,	`goal1`,	`goal2`,	`goal3`,	`goal4`,	`goal5`	) values(	:tMission.id,	:tMission.name,	:tMission.acceptCond1,	:tMission.acceptCond2,	:tMission.acceptCond3,	:tMission.acceptCond4,	:tMission.acceptCond5,	:tMission.goal1,	:tMission.goal2,	:tMission.goal3,	:tMission.goal4,	:tMission.goal5	)")
	Integer insert(@Arg(value="tMission") TMission o);
	
	@Sql(value="delete from t_mission where id=:id")
	void delete(@Arg(value="id")Integer id);
	
	@Sql(value="update t_mission set 	`id`=:tMission.id,	`name`=:tMission.name,	`acceptCond1`=:tMission.acceptCond1,	`acceptCond2`=:tMission.acceptCond2,	`acceptCond3`=:tMission.acceptCond3,	`acceptCond4`=:tMission.acceptCond4,	`acceptCond5`=:tMission.acceptCond5,	`goal1`=:tMission.goal1,	`goal2`=:tMission.goal2,	`goal3`=:tMission.goal3,	`goal4`=:tMission.goal4,	`goal5`=:tMission.goal5	 where id=:tMission.id")
	void update(@Arg(value="tMission") TMission o);

	@Sql(value="select * from t_mission where id=:id")
	TMission get(@Arg(value="id") Integer id);
	
	@Sql(value="select * from t_mission")
	List<TMission> getAll();
}

