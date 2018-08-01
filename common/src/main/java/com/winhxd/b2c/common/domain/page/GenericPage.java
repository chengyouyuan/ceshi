package com.winhxd.b2c.common.domain.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author 魏冰
 * @date 2017年4月6日 下午1:40:56
 * @Description 通用分页组件类
 * @version 1.0
 */
public class GenericPage<T> implements Page<T>,Serializable {

	private static final long serialVersionUID = -3751419583339078335L;

	public final static long DEFAULT_PAGE_SIZE = 20;

	protected long pageNo = 1;
	protected long pageSize = DEFAULT_PAGE_SIZE;
	protected long totalRows = 0;
	protected List<T> data = new ArrayList<>();

	public GenericPage() {
		super();
	}

	public GenericPage(long totalRows, List<T> data) {
		this();
		this.totalRows = totalRows;
		this.data = data;
	}

	public GenericPage(long pageNo, long totalRows, List<T> data) {
		this(totalRows, data);
		this.pageNo = pageNo;
	}

	public GenericPage(long pageNo, long totalRows, long pageSize, List<T> data) {
		this(totalRows, data);
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	@Override
	public long getTotalPages() {
		long totalPages = totalRows / pageSize;
		if (totalRows % pageSize > 0) {
			totalPages++;
		}
		return totalPages;
	}

	@Override
	public long getPageNo() {
		return pageNo;
	}

	public void setPageNo(long pageNo) {
		this.pageNo = pageNo;
		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	public long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public long getPreviousPageNo() {
		if (hasPreviousPage()) {
			return pageNo - 1;
		} else {
			return pageNo;
		}
	}

	@Override
	public long getNextPageNo() {
		if (hasNextPage()) {
			return pageNo + 1;
		} else {
			return pageNo;
		}
	}

	public boolean hasPreviousPage() {
		return (pageNo - 1 >= 1);
	}

	public boolean hasNextPage() {
		return (pageNo + 1 <= getTotalPages());
	}

}