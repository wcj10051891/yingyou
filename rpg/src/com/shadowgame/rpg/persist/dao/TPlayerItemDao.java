package com.shadowgame.rpg.persist.dao;
import java.util.Collection;
import java.util.List;

import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.BatchInsert;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;

import com.shadowgame.rpg.persist.entity.TPlayerItem;
@Dao
public interface TPlayerItemDao {
	@Sql(value="insert into t_player_item(	`id`,	`itemId`,	`playerId`,	`binding`,	`num`,	`strengthenLv`,	`hole`,	`createTime`	) values(	:tPlayerItem.id,	:tPlayerItem.itemId,	:tPlayerItem.playerId,	:tPlayerItem.binding,	:tPlayerItem.num,	:tPlayerItem.strengthenLv,	:tPlayerItem.hole,	:tPlayerItem.createTime	)")
	void insert(@Arg(value="tPlayerItem") TPlayerItem o);

	@Sql(value="insert into t_player_item(	`id`,	`itemId`,	`playerId`,	`binding`,	`num`,	`strengthenLv`,	`hole`,	`createTime`	) values(	:playerItem.id,	:playerItem.itemId,	:playerItem.playerId,	:playerItem.binding,	:playerItem.num,	:playerItem.strengthenLv,	:playerItem.hole,	:playerItem.createTime	)")
	void insertBatch(@BatchInsert("playerItem") Collection<TPlayerItem> playerItems);
	
	@Sql(value="delete from t_player_item where id=:id")
	void delete(@Arg(value="id")Long id);
	
	@Sql(value="update t_player_item set 	`id`=:tPlayerItem.id,	`itemId`=:tPlayerItem.itemId,	`playerId`=:tPlayerItem.playerId,	`binding`=:tPlayerItem.binding,	`num`=:tPlayerItem.num,	`strengthenLv`=:tPlayerItem.strengthenLv,	`hole`=:tPlayerItem.hole,	`createTime`=:tPlayerItem.createTime	 where id=:tPlayerItem.id")
	void update(@Arg(value="tPlayerItem") TPlayerItem o);

	@Sql(value="select * from t_player_item where id=:id")
	TPlayerItem get(@Arg(value="id") Long id);
	
	@Sql(value="select * from t_player_item")
	List<TPlayerItem> getAll();
}

