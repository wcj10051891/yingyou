package com.shadowgame.rpg.persist.dao;
import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;
import java.util.List;
import com.shadowgame.rpg.persist.entity.TBuff;
@Dao
public interface TBuffDao {
	@Sql(value="insert into t_buff(	`id`,	`name`,	`desc`,	`param`,	`period`,	`count`,	`duration`	) values(	:tBuff.id,	:tBuff.name,	:tBuff.desc,	:tBuff.param,	:tBuff.period,	:tBuff.count,	:tBuff.duration	)")
	Integer insert(@Arg(value="tBuff") TBuff o);
	
	@Sql(value="delete from t_buff where id=:id")
	void delete(@Arg(value="id")Integer id);
	
	@Sql(value="update t_buff set 	`id`=:tBuff.id,	`name`=:tBuff.name,	`desc`=:tBuff.desc,	`param`=:tBuff.param,	`period`=:tBuff.period,	`count`=:tBuff.count,	`duration`=:tBuff.duration	 where id=:tBuff.id")
	void update(@Arg(value="tBuff") TBuff o);

	@Sql(value="select * from t_buff where id=:id")
	TBuff get(@Arg(value="id") Integer id);
	
	@Sql(value="select * from t_buff")
	List<TBuff> getAll();
}

