package com.cmit.mvne.billing.user.analysis.common;

public class ReturnData<T> {

	private String code = "";

	private String message = "";

	private T returnObject ;

	public ReturnData() {
		super();
	}

	public ReturnData(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public ReturnData(String code, String message, T returnObject) {
		super();
		this.code = code;
		this.message = message;
		this.returnObject = returnObject;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getReturnObject() {
		return returnObject;
	}

	public void setReturnObject(T returnObject) {
		this.returnObject = returnObject;
	}

}
