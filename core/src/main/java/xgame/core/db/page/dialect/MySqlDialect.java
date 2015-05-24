package xgame.core.db.page.dialect;

public class MySqlDialect extends Dialect {
	public String getLimitString(String sql, int offset, int limit) {
		return sql + (offset > 0 ? " limit "+ offset +", "+ limit : " limit " + limit);
	}
}