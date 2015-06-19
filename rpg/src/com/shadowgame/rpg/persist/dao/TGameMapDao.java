package com.shadowgame.rpg.persist.dao;
import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;
import java.util.List;
import com.shadowgame.rpg.persist.entity.TGameMap;
@Dao
public interface TGameMapDao {
	@Sql(value="insert into t_game_map(	`id`,	`name`,	`width`,	`height`	) values(	:tGameMap.id,	:tGameMap.name,	:tGameMap.width,	:tGameMap.height	)")
	Integer insert(@Arg(value="tGameMap") TGameMap o);
	
	@Sql(value="delete from t_game_map where id=:id")
	void delete(@Arg(value="id")Integer id);
	
	@Sql(value="update t_game_map set 	`id`=:tGameMap.id,	`name`=:tGameMap.name,	`width`=:tGameMap.width,	`height`=:tGameMap.height	 where id=:tGameMap.id")
	void update(@Arg(value="tGameMap") TGameMap o);

	@Sql(value="select * from t_game_map where id=:id")
	TGameMap get(@Arg(value="id") Integer id);
	
	@Sql(value="select * from t_game_map")
	List<TGameMap> getAll();
}

