package com.shadowgame.rpg.persist.dao;
import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;
import java.util.List;
import com.shadowgame.rpg.persist.entity.Mission;
@Dao
public interface MissionDao {
	@Sql(value="insert into mission(	`id`,	`name`,	`acceptCond1`,	`acceptCond2`,	`acceptCond3`,	`acceptCond4`,	`acceptCond5`,	`goal1`,	`goal2`,	`goal3`,	`goal4`,	`goal5`	) values(	:mission.id,	:mission.name,	:mission.acceptCond1,	:mission.acceptCond2,	:mission.acceptCond3,	:mission.acceptCond4,	:mission.acceptCond5,	:mission.goal1,	:mission.goal2,	:mission.goal3,	:mission.goal4,	:mission.goal5	)")
	Integer insert(@Arg(value="mission") Mission o);
	
	@Sql(value="delete from mission where id=:id")
	void delete(@Arg(value="id")Integer id);
	
	@Sql(value="update mission set 	`id`=:mission.id,	`name`=:mission.name,	`acceptCond1`=:mission.acceptCond1,	`acceptCond2`=:mission.acceptCond2,	`acceptCond3`=:mission.acceptCond3,	`acceptCond4`=:mission.acceptCond4,	`acceptCond5`=:mission.acceptCond5,	`goal1`=:mission.goal1,	`goal2`=:mission.goal2,	`goal3`=:mission.goal3,	`goal4`=:mission.goal4,	`goal5`=:mission.goal5	 where id=:mission.id")
	void update(@Arg(value="mission") Mission o);

	@Sql(value="select * from mission where id=:id")
	Mission get(@Arg(value="id") Integer id);
	
	@Sql(value="select * from mission")
	List<Mission> getAll();
}

