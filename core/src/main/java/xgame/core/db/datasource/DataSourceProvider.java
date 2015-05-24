package xgame.core.db.datasource;

import javax.sql.DataSource;

/**
 * dataSource提供者，可获取数据库DataSource
 * @author wcj10051891@gmail.com
 */
public interface DataSourceProvider {
	DataSource getDataSource();
}
