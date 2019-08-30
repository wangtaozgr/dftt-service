package com.atao.dftt.api.vo;

import java.io.Serializable;

public class DataVo implements Serializable {
	private static final long serialVersionUID = -7774364298126020100L;
	private int status;
	private String msg;
	private Object data;

	public DataVo() {
		super();
	}

	public DataVo(int status, String msg, Object data) {
		super();
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
