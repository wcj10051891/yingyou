package com.shadowgame.rpg.persist.dao;
import java.util.List;

import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;

import com.shadowgame.rpg.persist.entity.Skill;
@Dao
public interface SkillDao {
	@Sql(value="select * from skill where id=:id")
	Skill get(@Arg(value="id") Integer id);
	
	@Sql(value="select * from skill")
	List<Skill> getAll();
}

