package daogen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class TableInfo {
	private Config cfg;
	private Map<String, TableMetaData> tableInfoCache;
	public TableInfo(Config cfg) {
		this.cfg = cfg;
		this.tableInfoCache = new LinkedHashMap<String, TableMetaData>();
		init();
	}
	private void init() {
		String url = cfg.getString("metadata.infoschema.url");
		String username = cfg.getString("metadata.username");
		String password = cfg.getString("metadata.password");
		String tableSchema = cfg.getString("metadata.target.tableschema");

		Connection conn = null;

		String tableSql = "SELECT table_name, table_comment FROM TABLES WHERE TABLE_SCHEMA='" + tableSchema +"'";
		String columnSql = "SELECT table_name, column_name, column_comment, IS_NULLABLE, "
				+ "DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, column_default FROM COLUMNS WHERE TABLE_SCHEMA='"+tableSchema+"'";

		try {
		    Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
			if(conn == null)
				throw new RuntimeException("Connection can not null.");

			PreparedStatement ps = conn.prepareStatement(tableSql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				TableMetaData tmd = new TableMetaData();
				tmd.setTableComment(rs.getString(2));
				tmd.setTableName(rs.getString(1));
				tableInfoCache.put(rs.getString(1), tmd);
			}
			rs.close();

			rs = ps.executeQuery(columnSql);

			while (rs.next()) {
				ColumnMetaData cmd = new ColumnMetaData();
				String tableName = rs.getString(1);
				cmd.setTableName(tableName);
				cmd.setColumnName(rs.getString(2));
				cmd.setColumnComment(rs.getString(3));
				cmd.setNullable(rs.getString(4));
				cmd.setColumnType(rs.getString(5));
				cmd.setColumnSize(rs.wasNull() ? Integer.MAX_VALUE : rs .getInt((6)));
				cmd.setColumnDefault(rs.getString(7));

				TableMetaData tmd = tableInfoCache.get(tableName);
				if (tmd != null) {
					tmd.addColumns(cmd);
				}
			}
			ps.close();
		} catch (Exception e) {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e1) {
				}
			throw new RuntimeException("table info load error.", e);
		}
	}
	
	public Map<String, TableMetaData> get(){
		return this.tableInfoCache;
	}
}
