package xgame.core.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置缓存对象延迟保存的秒数
 * @author wcj10051891@gmail.com
 * @date 2015年6月13日 下午1:53:05
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SaveDelay {
	int seconds() default 0;
}
