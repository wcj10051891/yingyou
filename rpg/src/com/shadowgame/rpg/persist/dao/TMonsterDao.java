package com.shadowgame.rpg.persist.dao;
import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;
import java.util.List;
import com.shadowgame.rpg.persist.entity.TMonster;
@Dao
public interface TMonsterDao {
	@Sql(value="insert into t_monster(	`id`,	`name`,	`boss`,	`mapId`,	`x`,	`y`	) values(	:tMonster.id,	:tMonster.name,	:tMonster.boss,	:tMonster.mapId,	:tMonster.x,	:tMonster.y	)")
	Integer insert(@Arg(value="tMonster") TMonster o);
	
	@Sql(value="delete from t_monster where id=:id")
	void delete(@Arg(value="id")Integer id);
	
	@Sql(value="update t_monster set 	`id`=:tMonster.id,	`name`=:tMonster.name,	`boss`=:tMonster.boss,	`mapId`=:tMonster.mapId,	`x`=:tMonster.x,	`y`=:tMonster.y	 where id=:tMonster.id")
	void update(@Arg(value="tMonster") TMonster o);

	@Sql(value="select * from t_monster where id=:id")
	TMonster get(@Arg(value="id") Integer id);
	
	@Sql(value="select * from t_monster")
	List<TMonster> getAll();
}

