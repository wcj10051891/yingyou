package daogen;

import java.io.Serializable;

public class ColumnMetaData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3604392321068082474L;

	private String tableName;
	private String columnName;
	private String columnComment;
	/*
	 * 	int
		varchar
		text
		tinyint
		bigint
		datetime
		timestamp
	 */
	private String columnType;
	
	private String nullable;//YES or NO
	private int columnSize;
	private String columnDefault;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getNullable() {
		return nullable;
	}

	public void setNullable(String nullable) {
		this.nullable = nullable;
	}

	public int getColumnSize() {
		return columnSize;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

    public String getColumnDefault()
    {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault)
    {
        this.columnDefault = columnDefault;
    }
}
