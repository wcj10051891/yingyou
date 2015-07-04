package com.shadowgame.rpg.persist.dao;
import java.util.List;

import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;

import com.shadowgame.rpg.persist.entity.TItem;
@Dao
public interface TItemDao {
	@Sql(value="insert into t_item(	`id`,	`name`,	`desc`,	`playerLv`,	`parentType`,	`itemType`,	`bindType`,	`quality`,	`maxStack`,	`function`,	`extAttribute`	) values(	:tItem.id,	:tItem.name,	:tItem.desc,	:tItem.playerLv,	:tItem.parentType,	:tItem.itemType,	:tItem.bindType,	:tItem.quality,	:tItem.maxStack,	:tItem.function,	:tItem.extAttribute	)")
	Integer insert(@Arg(value="tItem") TItem o);
	
	@Sql(value="delete from t_item where id=:id")
	void delete(@Arg(value="id")Integer id);
	
	@Sql(value="update t_item set 	`id`=:tItem.id,	`name`=:tItem.name,	`desc`=:tItem.desc,	`playerLv`=:tItem.playerLv,	`parentType`=:tItem.parentType,	`itemType`=:tItem.itemType,	`bindType`=:tItem.bindType,	`quality`=:tItem.quality,	`maxStack`=:tItem.maxStack,	`function`=:tItem.function,	`extAttribute`=:tItem.extAttribute	 where id=:tItem.id")
	void update(@Arg(value="tItem") TItem o);

	@Sql(value="select * from t_item where id=:id")
	TItem get(@Arg(value="id") Integer id);
	
	@Sql(value="select * from t_item")
	List<TItem> getAll();
	
	@Sql(value="select * from t_item where id in(:ids)")
	List<TItem> getByItemIds(@Arg(value="ids") List<Integer> ids);
}

