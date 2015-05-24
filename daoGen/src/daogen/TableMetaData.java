package daogen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TableMetaData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4962661936916601301L;

	private String tableName;
	private String tableComment;
    private List<ColumnMetaData> columns = new ArrayList<ColumnMetaData>();
    private List<String> columnNames = new ArrayList<String>();

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableComment() {
		return tableComment;
	}

	public String getFixTableComment() {
		if (tableComment.isEmpty())
			return tableComment;
		return tableComment.replaceAll(";.*", "");
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

	public List<ColumnMetaData> getColumns() {
		return columns;
	}
	
	public void addColumns(ColumnMetaData data)
	{
	    columns.add(data);
	    columnNames.add(data.getColumnName());
	}
	
	public List<String> getColumnNames()
	{
	    return columnNames;
	}

	public void setColumns(List<ColumnMetaData> columns) {
		this.columns = columns;
	}
}
