package com.atao.base.vo;

import java.io.Serializable;

/**
 * 基础vo
 */
public class BaseVo implements Serializable {

	private Integer page;

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