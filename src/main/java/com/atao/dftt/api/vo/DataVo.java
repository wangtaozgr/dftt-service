package com.atao.dftt.api.vo;

import java.io.Serializable;

public class DataVo implements Serializable {
	private static final long serialVersionUID = -7774364298126020100L;
	private int status;
	private String msg;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
