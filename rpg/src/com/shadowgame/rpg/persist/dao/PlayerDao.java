package com.shadowgame.rpg.persist.dao;
import java.util.List;

import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;

import com.shadowgame.rpg.persist.entity.Player;
@Dao
public interface PlayerDao {
	@Sql(value="insert into t_player(	`id`,	`username`,	`nickname`,	`lv`,	`exp`,	`createTime`,	`loginTime`,	`logoutTime`,	`daily`,	`extAttribute`	) values(	:player.id,	:player.username,	:player.nickname,	:player.lv,	:player.exp,	:player.createTime,	:player.loginTime,	:player.logoutTime,	:player.daily,	:player.extAttribute	)")
	void insert(@Arg(value="player") Player o);
	
	@Sql(value="delete from t_player where id=:id")
	void delete(@Arg(value="id")Long id);
	
	@Sql(value="update t_player set 	`id`=:player.id,	`username`=:player.username,	`nickname`=:player.nickname,	`lv`=:player.lv,	`exp`=:player.exp,	`createTime`=:player.createTime,	`loginTime`=:player.loginTime,	`logoutTime`=:player.logoutTime,	`daily`=:player.daily,	`extAttribute`=:player.extAttribute	 where id=:player.id")
	void update(@Arg(value="player") Player o);

	@Sql(value="select * from t_player where id=:id")
	Player get(@Arg(value="id") Long id);
	
	@Sql(value="select * from t_player where username=:username")
	Player getByUsername(@Arg(value="username") String username);
	
	@Sql(value="select * from t_player")
	List<Player> getAll();
}

