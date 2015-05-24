package xgame.core.db.page.dialect;

public class SqlServerDialect extends Dialect {
	@Override
	public String getLimitString(String sql, int offset, int limit) {
		if (offset > 0)
			return "";

		int selectIndex = sql.toLowerCase().indexOf("select");
		int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
		return new StringBuffer(sql.length() + 8)
			.append(sql).insert(selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6), " top " + limit).toString();
	}
}
