package com.shadowgame.rpg.persist.dao;
import java.util.List;

import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;

import com.shadowgame.rpg.persist.entity.Item;
@Dao
public interface ItemDao {
	@Sql(value="insert into item(	`id`,	`name`,	`description`,	`playerLv`,	`parentType`,	`itemType`,	`bindType`,	`quality`,	`maxStack`,	`function`,	`extAttribute`	) values(	:item.id,	:item.name,	:item.description,	:item.playerLv,	:item.parentType,	:item.itemType,	:item.bindType,	:item.quality,	:item.maxStack,	:item.function,	:item.extAttribute	)")
	Integer insert(@Arg(value="item") Item o);
	
	@Sql(value="delete from item where id=:id")
	void delete(@Arg(value="id")Integer id);
	
	@Sql(value="update item set 	`id`=:item.id,	`name`=:item.name,	`description`=:item.description,	`playerLv`=:item.playerLv,	`parentType`=:item.parentType,	`itemType`=:item.itemType,	`bindType`=:item.bindType,	`quality`=:item.quality,	`maxStack`=:item.maxStack,	`function`=:item.function,	`extAttribute`=:item.extAttribute	 where id=:item.id")
	void update(@Arg(value="item") Item o);

	@Sql(value="select * from item where id=:id")
	Item get(@Arg(value="id") Integer id);

	@Sql(value="select * from item where id in(:ids)")
	List<Item> getByItemIds(@Arg(value="ids") List<Integer> ids);
	
	@Sql(value="select * from item")
	List<Item> getAll();
}

