package xgame.core.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定dao接口方法要执行的sql，若该方法未标记@Sql，就看方法的参数列表中有没有包含@Sql标记的String类型方法参数，
 * 有的话就取这个动态的sql去执行，否则都没找到就报错。
 * @author wcj
 */
@Target( { ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sql
{
    String value() default "";
}