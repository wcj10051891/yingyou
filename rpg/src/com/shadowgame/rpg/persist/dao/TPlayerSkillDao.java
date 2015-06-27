package com.shadowgame.rpg.persist.dao;
import java.util.List;

import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;

import com.shadowgame.rpg.persist.entity.TPlayerSkill;
@Dao
public interface TPlayerSkillDao {
	@Sql(value="insert into t_player_skill(	`id`,	`skills`	) values(	:tPlayerSkill.id,	:tPlayerSkill.skills	)")
	void insert(@Arg(value="tPlayerSkill") TPlayerSkill o);
	
	@Sql(value="delete from t_player_skill where id=:id")
	void delete(@Arg(value="id")Integer id);
	
	@Sql(value="update t_player_skill set 	`id`=:tPlayerSkill.id,	`skills`=:tPlayerSkill.skills	 where id=:tPlayerSkill.id")
	void update(@Arg(value="tPlayerSkill") TPlayerSkill o);

	@Sql(value="select * from t_player_skill where id=:id")
	TPlayerSkill get(@Arg(value="id") Integer id);
	
	@Sql(value="select * from t_player_skill")
	List<TPlayerSkill> getAll();
}

