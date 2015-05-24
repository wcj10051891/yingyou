package xgame.core.db;

import javax.sql.DataSource;

import xgame.core.db.datasource.DataSourceProvider;
import xgame.core.db.datasource.SimpleDataSourceProvider;
import xgame.core.util.Config;

public class DaoContext {
	public static final String DataSourceProviderClass = "dataSource.providerClass";
	
	public TransactionalQueryRunner jdbc;
	public DaoFactory daoFactory;

	public DaoContext(DataSource dataSource) {
		this.init(dataSource);
	}
	public DaoContext() {
		this.init(initDataSource());
	}
	
	private void init(DataSource dataSource) {
		boolean defaultAutoCommit = false;
		try {
			defaultAutoCommit = dataSource.getConnection().getAutoCommit();
		} catch (Exception e) {
			throw new DaoException("datasource init error.", e);
		}
		jdbc = new TransactionalQueryRunner(dataSource, defaultAutoCommit);
		daoFactory = new DaoFactory(jdbc);
	}

	private DataSource initDataSource() {
		Config cfg = new Config("jdbc.properties");
		DataSourceProvider provider = null;
		try {
			provider = (DataSourceProvider)Class.forName(cfg.getString(DataSourceProviderClass)).getConstructor(Config.class).newInstance(cfg);
		} catch (Exception e) {
			provider = new SimpleDataSourceProvider(cfg);
		}
		return provider.getDataSource();
	}
}
