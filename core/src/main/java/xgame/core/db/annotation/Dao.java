package xgame.core.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记dao接口，被标记后的用法：
 * <br>XXDao dao = DaoFactory.get(XXDao.class);</br>
 * 获得dao引用后就可以直接调用dao上的方法来执行@Sql提供的sql语句
 * @author wcj
 */
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Dao
{
}
