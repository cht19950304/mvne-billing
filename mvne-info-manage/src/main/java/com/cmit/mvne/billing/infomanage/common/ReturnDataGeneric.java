package com.cmit.mvne.billing.infomanage.common;

import java.util.Map;

public class ReturnDataGeneric<T> {
	
	private String code = "";

	private String message = "";
	
	private T returnObject;
	
	private Map<String, Object> returnMap;
	
	public ReturnDataGeneric() {
		super();
	}
	
	public ReturnDataGeneric(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public ReturnDataGeneric(String code, String message, T returnObject) {
		super();
		this.code = code;
		this.message = message;
		this.returnObject = returnObject;
	}

	public ReturnDataGeneric(String code, String message, T returnObject,
			Map<String, Object> returnMap) {
		super();
		this.code = code;
		this.message = message;
		this.returnObject = returnObject;
		this.returnMap = returnMap;
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

	public Map<String, Object> getReturnMap() {
		return returnMap;
	}

	public void setReturnMap(Map<String, Object> returnMap) {
		this.returnMap = returnMap;
	}

}
