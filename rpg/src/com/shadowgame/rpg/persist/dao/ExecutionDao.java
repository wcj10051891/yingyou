package com.shadowgame.rpg.persist.dao;
import xgame.core.db.annotation.Arg;
import xgame.core.db.annotation.Dao;
import xgame.core.db.annotation.Sql;
import java.util.List;
import com.shadowgame.rpg.persist.entity.Execution;
@Dao
public interface ExecutionDao {
	@Sql(value="insert into execution(	`key`,	`clazz`,	`method`,	`param`	) values(	:execution.key,	:execution.clazz,	:execution.method,	:execution.param	)")
	Integer insert(@Arg(value="execution") Execution o);
	
	@Sql(value="delete from execution where id=:id")
	void delete(@Arg(value="id")Integer id);
	
	@Sql(value="update execution set 	`key`=:execution.key,	`clazz`=:execution.clazz,	`method`=:execution.method,	`param`=:execution.param	 where id=:execution.id")
	void update(@Arg(value="execution") Execution o);

	@Sql(value="select * from execution where id=:id")
	Execution get(@Arg(value="id") Integer id);
	
	@Sql(value="select * from execution")
	List<Execution> getAll();
}

