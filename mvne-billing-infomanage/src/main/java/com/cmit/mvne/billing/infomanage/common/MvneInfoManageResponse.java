/**
 * 
 */
package com.cmit.mvne.billing.infomanage.common;

import java.io.Serializable;
import java.util.HashMap;

import org.springframework.http.HttpStatus;

/**
 * @author jiangxm02
 *
 */
public class MvneInfoManageResponse extends HashMap<String, Object> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8260539915914813366L;

	public MvneInfoManageResponse code(HttpStatus status) {
        this.put("code", status.value());
        return this;
    }

    public MvneInfoManageResponse message(String message) {
        this.put("message", message);
        return this;
    }

    public MvneInfoManageResponse data(Object data) {
        this.put("data", data);
        return this;
    }

    public MvneInfoManageResponse success() {
        this.code(HttpStatus.OK);
        return this;
    }

    public MvneInfoManageResponse fail() {
        this.code(HttpStatus.INTERNAL_SERVER_ERROR);
        return this;
    }

    public MvneInfoManageResponse servfail(String sevCode) {
        this.servCode(sevCode);
        return this;
    }

    public MvneInfoManageResponse servCode(String sevCode) {
        this.put("code",sevCode);
        return this;
    }

    @Override
    public MvneInfoManageResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
