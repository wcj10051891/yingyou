package com.shadowgame.rpg.persist.dao;
import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;
import java.util.List;
import com.shadowgame.rpg.persist.entity.TSkill;
@Dao
public interface TSkillDao {
	@Sql(value="insert into t_skill(	`id`,	`name`,	`description`,	`vocation`,	`distance`,	`target`,	`param`	) values(	:tSkill.id,	:tSkill.name,	:tSkill.description,	:tSkill.vocation,	:tSkill.distance,	:tSkill.target,	:tSkill.param	)")
	Integer insert(@Arg(value="tSkill") TSkill o);
	
	@Sql(value="delete from t_skill where id=:id")
	void delete(@Arg(value="id")Integer id);
	
	@Sql(value="update t_skill set 	`id`=:tSkill.id,	`name`=:tSkill.name,	`description`=:tSkill.description,	`vocation`=:tSkill.vocation,	`distance`=:tSkill.distance,	`target`=:tSkill.target,	`param`=:tSkill.param	 where id=:tSkill.id")
	void update(@Arg(value="tSkill") TSkill o);

	@Sql(value="select * from t_skill where id=:id")
	TSkill get(@Arg(value="id") Integer id);
	
	@Sql(value="select * from t_skill")
	List<TSkill> getAll();
}

