package com.atao.base.support;

import java.util.Collection;
import java.util.List;

import com.github.pagehelper.Page;

public class MyPage<T> {
	protected int pageNum;
	protected int pageSize;

	protected long total;
	protected List<T> list;

	public MyPage() {
	}

	public MyPage(List<T> list) {
		if (list instanceof Page) {
			Page page = (Page) list;
			this.list = page;
			this.total = page.getTotal();
			this.pageNum = page.getPageNum();
			this.pageSize = page.getPageSize();
		} else if (list instanceof Collection) {
			this.list = list;
			this.total = (long) list.size();
			this.pageNum = 1;
			this.pageSize = list.size();
		}
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
}
