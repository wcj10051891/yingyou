package com.shadowgame.rpg.persist.dao;
import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;

import com.shadowgame.rpg.persist.entity.TPlayerKnapsack;
@Dao
public interface TPlayerKnapsackDao {
	@Sql(value="insert into t_player_knapsack(	`id`,	`items`,	`capacity`	) values(	:tPlayerKnapsack.id,	:tPlayerKnapsack.items,	:tPlayerKnapsack.capacity	)")
	void insert(@Arg(value="tPlayerKnapsack") TPlayerKnapsack o);
	
	@Sql(value="delete from t_player_knapsack where id=:id")
	void delete(@Arg(value="id")Long id);
	
	@Sql(value="update t_player_knapsack set 	`id`=:tPlayerKnapsack.id,	`items`=:tPlayerKnapsack.items,	`capacity`=:tPlayerKnapsack.capacity	 where id=:tPlayerKnapsack.id")
	void update(@Arg(value="tPlayerKnapsack") TPlayerKnapsack o);

	@Sql(value="select * from t_player_knapsack where id=:id")
	TPlayerKnapsack get(@Arg(value="id") Long id);
}

