package com.shadowgame.rpg.persist.dao;
import java.util.List;

import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;

import com.shadowgame.rpg.persist.entity.TPlayer;
@Dao
public interface TPlayerDao {
	@Sql(value="insert into t_player(	`id`,	`username`,	`nickname`,	`vocation`,	`lv`,	`exp`,	`createTime`,	`loginTime`,	`logoutTime`,	`daily`,	`extAttribute`,	`lastMapId`,	`lastInstanceId`,	`lastMapX`,	`lastMapY`,	`skill`,	`buff`	) values(	:tPlayer.id,	:tPlayer.username,	:tPlayer.nickname,	:tPlayer.vocation,	:tPlayer.lv,	:tPlayer.exp,	:tPlayer.createTime,	:tPlayer.loginTime,	:tPlayer.logoutTime,	:tPlayer.daily,	:tPlayer.extAttribute,	:tPlayer.lastMapId,	:tPlayer.lastInstanceId,	:tPlayer.lastMapX,	:tPlayer.lastMapY,	:tPlayer.skill,	:tPlayer.buff	)")
	void insert(@Arg(value="tPlayer") TPlayer o);
	
	@Sql(value="delete from t_player where id=:id")
	void delete(@Arg(value="id")Long id);
	
	@Sql(value="update t_player set 	`id`=:tPlayer.id,	`username`=:tPlayer.username,	`nickname`=:tPlayer.nickname,	`vocation`=:tPlayer.vocation,	`lv`=:tPlayer.lv,	`exp`=:tPlayer.exp,	`createTime`=:tPlayer.createTime,	`loginTime`=:tPlayer.loginTime,	`logoutTime`=:tPlayer.logoutTime,	`daily`=:tPlayer.daily,	`extAttribute`=:tPlayer.extAttribute,	`lastMapId`=:tPlayer.lastMapId,	`lastInstanceId`=:tPlayer.lastInstanceId,	`lastMapX`=:tPlayer.lastMapX,	`lastMapY`=:tPlayer.lastMapY,	`skill`=:tPlayer.skill,	`buff`=:tPlayer.buff	 where id=:tPlayer.id")
	void update(@Arg(value="tPlayer") TPlayer o);

	@Sql(value="select * from t_player where id=:id")
	TPlayer get(@Arg(value="id") Long id);
	
	@Sql(value="select * from t_player where username=:username")
	List<TPlayer> getByUsername(@Arg(value="username") String username);
	
	@Sql(value="select * from t_player where nickname=:nickname")
	TPlayer getByNickname(@Arg(value="nickname") String nickname);
	
	@Sql(value="select * from t_player")
	List<TPlayer> getAll();
}

