package com.atao.base.vo;

public class DataVO {
	private boolean success;
	private String message;// 调用出错时的错误信息
	private Object data;

	public DataVO(boolean success, String message, Object data) {
		this.success = success;
		this.message = message;
		this.data = data;

	}

	public DataVO() {
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
