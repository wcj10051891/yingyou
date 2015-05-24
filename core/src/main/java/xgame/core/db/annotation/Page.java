package xgame.core.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分页支持，value指定分页查询要返回的类型
 * <br>example:</br>
 * <pre>@Sql("select * from active")
 * public PageResult getPage(@Page(Active.class) PageResult page);</pre>
 * <pre>@Sql("select name from active where name in(:names)")
 * public PageResult getPage(@Page(String.class) PageResult page, @Arg("names")List&lt;String&gt; names);</pre>
 * @author wcj
 */
@Target( { ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Page {
    Class<?> value();
}
