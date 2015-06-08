package com.shadowgame.rpg.persist.dao;
import java.util.Collection;
import java.util.List;

import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.BatchInsert;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;

import com.shadowgame.rpg.persist.entity.PlayerItem;
@Dao
public interface PlayerItemDao {
	@Sql(value="insert into player_item(	`id`,	`itemId`,	`playerId`,	`binding`,	`num`,	`strengthenLevel`,	`hole`,	`createTime`	) values(	:playerItem.id,	:playerItem.itemId,	:playerItem.playerId,	:playerItem.binding,	:playerItem.num,	:playerItem.strengthenLevel,	:playerItem.hole,	:playerItem.createTime	)")
	void insert(@Arg(value="playerItem") PlayerItem o);
	
	@Sql(value="insert into player_item(	`id`,	`itemId`,	`playerId`,	`binding`,	`num`,	`strengthenLevel`,	`hole`,	`createTime`	) values(	:playerItem.id,	:playerItem.itemId,	:playerItem.playerId,	:playerItem.binding,	:playerItem.num,	:playerItem.strengthenLevel,	:playerItem.hole,	:playerItem.createTime	)")
	void insertBatch(@BatchInsert("playerItem") Collection<PlayerItem> playerItems);
	
	@Sql(value="delete from player_item where id=:id")
	void delete(@Arg(value="id")Long id);
	
	@Sql(value="update player_item set 	`id`=:playerItem.id,	`itemId`=:playerItem.itemId,	`playerId`=:playerItem.playerId,	`binding`=:playerItem.binding,	`num`=:playerItem.num,	`strengthenLevel`=:playerItem.strengthenLevel,	`hole`=:playerItem.hole,	`createTime`=:playerItem.createTime	 where id=:playerItem.id")
	void update(@Arg(value="playerItem") PlayerItem o);

	@Sql(value="select * from player_item where id=:id")
	PlayerItem get(@Arg(value="id") Long id);
	
	@Sql(value="select * from player_item")
	List<PlayerItem> getAll();
}