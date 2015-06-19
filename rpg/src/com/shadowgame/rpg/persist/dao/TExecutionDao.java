package com.shadowgame.rpg.persist.dao;
import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;
import java.util.List;
import com.shadowgame.rpg.persist.entity.TExecution;
@Dao
public interface TExecutionDao {
	@Sql(value="insert into t_execution(	`key`,	`clazz`,	`method`,	`param`	) values(	:tExecution.key,	:tExecution.clazz,	:tExecution.method,	:tExecution.param	)")
	Integer insert(@Arg(value="tExecution") TExecution o);
	
	@Sql(value="delete from t_execution where id=:id")
	void delete(@Arg(value="id")Integer id);
	
	@Sql(value="update t_execution set 	`key`=:tExecution.key,	`clazz`=:tExecution.clazz,	`method`=:tExecution.method,	`param`=:tExecution.param	 where id=:tExecution.id")
	void update(@Arg(value="tExecution") TExecution o);

	@Sql(value="select * from t_execution where id=:id")
	TExecution get(@Arg(value="id") Integer id);
	
	@Sql(value="select * from t_execution")
	List<TExecution> getAll();
}

