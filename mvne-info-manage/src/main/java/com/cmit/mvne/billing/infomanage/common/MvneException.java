package com.cmit.mvne.billing.infomanage.common;

import lombok.Data;

@Data
public class MvneException extends Exception {
	/* 异常错误码
	 */
	private String errCode;
	/* 异常错误描述
	 */
	private String errDesc;

	private static final long serialVersionUID = 1L;

	public MvneException() {
		super();
	}

	public MvneException(String message) {
		super(message);
	}

	public MvneException(String message, Throwable cause) {
		super(message, cause);
	}

	public MvneException(Throwable cause) {
		super(cause);
	}

	public MvneException(String errCode, String errDesc) {
		this.errCode = errCode;
		this.errDesc = errDesc;
	}

}
