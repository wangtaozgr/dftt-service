package com.atao.base.model;

import java.io.Serializable;

import javax.persistence.Transient;

public class BaseEntity implements Serializable {

	@Transient
	private Integer page;

	@Transient
	private Integer rows;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public void initPage() {
		if (page == null)
			page = 1;
		if (rows == null) {
			rows = 10;
		}
	}
}
