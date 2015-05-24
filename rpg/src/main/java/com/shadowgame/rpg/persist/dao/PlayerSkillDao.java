package com.shadowgame.rpg.persist.dao;
import java.util.List;

import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;

import com.shadowgame.rpg.persist.entity.PlayerSkill;
@Dao
public interface PlayerSkillDao {
	@Sql(value="insert into player_skill(	`id`,	`skills`	) values(	:playerSkill.id,	:playerSkill.skills	)")
	void insert(@Arg(value="playerSkill") PlayerSkill o);
	
	@Sql(value="delete from player_skill where id=:id")
	void delete(@Arg(value="id")Long id);
	
	@Sql(value="update player_skill set 	`id`=:playerSkill.id,	`skills`=:playerSkill.skills	 where id=:playerSkill.id")
	void update(@Arg(value="playerSkill") PlayerSkill o);

	@Sql(value="select * from player_skill where id=:id")
	PlayerSkill get(@Arg(value="id") Long id);
	
	@Sql(value="select * from player_skill")
	List<PlayerSkill> getAll();
}

