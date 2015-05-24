package xgame.core.db.page;

import java.util.List;

import xgame.core.db.DaoException;

public class PageResult {
	protected int start;
	protected int pageSize;
	protected int total;
	protected int pageIndex;
	protected int pageCount;
	private List<?> data;

	public PageResult(int pageIndex, int pageSize) {
	    this.pageIndex = pageIndex;
	    if (pageIndex <= 0)
		throw new DaoException("page index value can not less than zero.");
	    this.pageSize = pageSize;
	    if (pageSize <= 0)
		throw new DaoException("page size value can not less than zero.");
	    this.start = (pageIndex - 1) * pageSize;
	}

	public int getStart() {
		return start;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotal() {
		return total;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void init(int total) {
		if (total < 0)
			throw new DaoException("page total records can not less than zero.");
		if (pageIndex <= 0)
			throw new DaoException("page index value can not less than zero.");
		if (pageSize <= 0)
			throw new DaoException("page size value can not less than zero.");
		this.total = total;
		this.pageCount = this.total < this.pageSize ? 1 : (int) Math
				.ceil((double) this.total / this.pageSize);
	}
}
