package com.cmit.mvne.billing.rating.gprs.creditcontrol;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Mvne Crm 通用返回实体类
 *
 * @author <a href="mailto:ference.zeng@gmail.com">Ference Zeng</a>
 * @since 2019-10-22
 */
public class MvneCrmResponse extends HashMap<String, Object> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2419750506386855971L;

	public MvneCrmResponse code(HttpStatus status) {
        this.put("code", status.value());
        return this;
    }

    public MvneCrmResponse message(String message) {
        this.put("message", message);
        return this;
    }

    public MvneCrmResponse data(Object data) {
        this.put("data", data);
        return this;
    }

    public MvneCrmResponse success() {
        this.code(HttpStatus.OK);
        return this;
    }

    public MvneCrmResponse fail() {
        this.code(HttpStatus.INTERNAL_SERVER_ERROR);
        return this;
    }

    @Override
    public MvneCrmResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }



}
