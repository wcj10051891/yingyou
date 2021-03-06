package com.shadowgame.rpg.persist.dao;
import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;
import java.util.List;
import com.shadowgame.rpg.persist.entity.TSkill;
@Dao
public interface TSkillDao {
	@Sql(value="insert into t_skill(	`id`,	`name`,	`desc`,	`vocation`,	`cooldown`,	`mp`,	`rangeType`,	`rangePosition`,	`rangeDistance`,	`rangeAngle`,	`rangeWidth`,	`delay`,	`period`,	`count`,	`damagePercent`	) values(	:tSkill.id,	:tSkill.name,	:tSkill.desc,	:tSkill.vocation,	:tSkill.cooldown,	:tSkill.mp,	:tSkill.rangeType,	:tSkill.rangePosition,	:tSkill.rangeDistance,	:tSkill.rangeAngle,	:tSkill.rangeWidth,	:tSkill.delay,	:tSkill.period,	:tSkill.count,	:tSkill.damagePercent	)")
	Integer insert(@Arg(value="tSkill") TSkill o);
	
	@Sql(value="delete from t_skill where id=:id")
	void delete(@Arg(value="id")Integer id);
	
	@Sql(value="update t_skill set 	`id`=:tSkill.id,	`name`=:tSkill.name,	`desc`=:tSkill.desc,	`vocation`=:tSkill.vocation,	`cooldown`=:tSkill.cooldown,	`mp`=:tSkill.mp,	`rangeType`=:tSkill.rangeType,	`rangePosition`=:tSkill.rangePosition,	`rangeDistance`=:tSkill.rangeDistance,	`rangeAngle`=:tSkill.rangeAngle,	`rangeWidth`=:tSkill.rangeWidth,	`delay`=:tSkill.delay,	`period`=:tSkill.period,	`count`=:tSkill.count,	`damagePercent`=:tSkill.damagePercent	 where id=:tSkill.id")
	void update(@Arg(value="tSkill") TSkill o);

	@Sql(value="select * from t_skill where id=:id")
	TSkill get(@Arg(value="id") Integer id);
	
	@Sql(value="select * from t_skill")
	List<TSkill> getAll();
}

