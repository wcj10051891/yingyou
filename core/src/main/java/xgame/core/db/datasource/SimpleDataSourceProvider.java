package xgame.core.db.datasource;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import xgame.core.db.DaoException;
import xgame.core.util.ClassUtils;
import xgame.core.util.Config;

/**
 * 默认实现，从配置文件读取配置来创建DataSource
 * dataSource.implementClass=实现类的完整类名（如：com.jolbox.bonecp.BoneCPDataSource）
 * dataSource.开头，后面是set方法名，用于设置DataSource属性，即调用指定的set方法。
 * 
 * dataSource.setDriverClass=com.mysql.jdbc.Driver
 * dataSource.setJdbcUrl=jdbc:mysql://localhost:3306/sakila?useUnicode=true&characterEncoding=utf8
 * dataSource.setUsername=root
 * dataSource.setPassword=root
 * 
 * @author wcj10051891@gmail.com
 */
public class SimpleDataSourceProvider implements DataSourceProvider {
	
	protected static final String propertyPrefix = "dataSource.";
	protected static final String implClass = propertyPrefix + "implementClass";
	protected Config config;
	
	public SimpleDataSourceProvider(Config config) {
		this.config = config;
	}

	@Override
	public DataSource getDataSource() {
		try {
			Class<?> dataSourceClass = Class.forName(this.config.getString(implClass));
			Map<String, Method> setMethods = new HashMap<String, Method>();
			for (Method method : dataSourceClass.getMethods()) {
				if (method.getName().startsWith("set")
						&& method.getParameterTypes().length == 1)
					setMethods.put(method.getName(), method);
			}
			Object dataSource = dataSourceClass.newInstance();
			Properties properties = this.config.getProperties();
			for (String key : properties.stringPropertyNames()) {
				if (key.startsWith(propertyPrefix)) {
					Method method = null;
					if ((method = setMethods.get(key.split("\\.")[1])) != null)
						method.invoke(dataSource, 
								ClassUtils.cast(properties.getProperty(key),
								method.getParameterTypes()[0]));
				}
			}
			return (DataSource)dataSource;
		} catch (Exception e) {
			throw new DaoException("dataSource init error.", e);
		}
	}
}