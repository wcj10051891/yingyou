package com.shadowgame.rpg.persist.dao;

import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;

import com.shadowgame.rpg.persist.entity.PlayerKnapsack;

@Dao
public interface PlayerKnapsackDao {
    @Sql(value = "update t_player_knapsack set 	`id`=:playerKnapsack.id,	`items`=:playerKnapsack.items,	`capacity`=:playerKnapsack.capacity	 where id=:playerKnapsack.id")
    void update(@Arg(value = "playerKnapsack") PlayerKnapsack o);

    @Sql(value = "insert into t_player_knapsack(	`id`,	`items`,	`capacity`	) values(	:playerKnapsack.id,	:playerKnapsack.items,	:playerKnapsack.capacity	)")
    void insert(@Arg(value = "playerKnapsack") PlayerKnapsack o);

    @Sql(value = "select * from t_player_knapsack where id=:id")
    PlayerKnapsack get(@Arg(value = "id") Long id);
}
