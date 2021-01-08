package com.cmit.mvne.billing.rating.gprs.creditcontrol;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/5/22 18:57
 */

@Data
public class CreditInfo {

    private BigDecimal balance = null;

    private BigDecimal leftFreeRes = null;

    private String msisdn;

    private String text;

    private String operation;

    private String reason;

    public CreditInfo() {
        super();
    }

    public CreditInfo(CreditInfo creditInfo) {
        this.balance = creditInfo.getBalance();
        this.leftFreeRes = creditInfo.getLeftFreeRes();
        this.msisdn = creditInfo.getMsisdn();
        this.text = creditInfo.getText();
        this.operation = creditInfo.getOperation();
        this.reason = creditInfo.getReason();
    }
}
