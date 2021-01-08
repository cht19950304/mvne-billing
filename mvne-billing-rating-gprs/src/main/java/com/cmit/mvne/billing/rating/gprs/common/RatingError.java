package com.cmit.mvne.billing.rating.gprs.common;

import lombok.Data;

import java.util.Date;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/5/8 15:01
 */

@Data
public class RatingError {
    String errCode;
    String errDesc;

    Date ratingTime;

    public Boolean isNotNull() {
        return ((this.errCode!=null) || (this.errDesc!=null));
    }

    public RatingError() {
        this.errCode = null;
        this.errDesc = null;
    }

    public RatingError(String errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;

        this.ratingTime = new Date();
    }
}
