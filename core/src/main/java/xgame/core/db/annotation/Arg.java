package xgame.core.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记dao类中方法的参数，指明@Sql中的待替换的参数别名
 * <br>example:  用alias做别名，生成sql语句会取用@Arg标记的对象上的字段值来替换</br> 
 * <pre>@Sql(value="insert into table(`id`,`name`) values(:alias.id,:alias.name)")
 * Integer insert(@Arg(value="alias") Active o);</pre>
 * @author wcj
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Arg {
	/**
	 * 待替换参数别名
	 * @return
	 */
	String value();
}