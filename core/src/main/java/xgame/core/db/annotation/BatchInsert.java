package xgame.core.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 批量插入数据，dao类的方法，传入Collection&lt;Entity&gt;，会为每个Entity生成一组insert语句的values值，最终合并成一条sql执行。
 * @author wcj
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BatchInsert {
	/**
	 * 待替换参数别名
	 * @see xgame.core.db.annotation.Arg
	 * @return
	 */
	String value();
}