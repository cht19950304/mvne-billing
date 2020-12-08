package com.cmit.mvne.billing.creditcontrol.exception;

import java.io.Serializable;

/**
 * Mvne Crm 系统内部异常类
 *
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2019/11/1
 */
public class MvneCrmException extends RuntimeException implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -1557947790670115136L;

	public MvneCrmException(String message) {
        super(message);
    }
}
