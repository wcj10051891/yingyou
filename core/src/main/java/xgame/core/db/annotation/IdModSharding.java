package xgame.core.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import xgame.core.db.sharding.IdModStrategy;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IdModSharding {
	
	/**
	 * 处理类
	 */
	Class<?> processClass() default IdModStrategy.class;
	/**
	 * 数据库表名称
	 * 如表名table_name_1
	 */
	String tableName();
	/**
	 * 切分表数目
	 */
	int shardingCount() default 10;
	/**
	 * 表名称中要替换的字符的regex
	 * 如表名table_name_1，则要替换部分为1
	 */
	String replaceRegex() default "\\d+$";
}
