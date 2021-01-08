package com.cmit.mvne.billing.rating.gprs.common;

import com.cmit.mvne.billing.rating.gprs.creditcontrol.CreditInfo;
import lombok.Data;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/7/9 11:20
 */

@Data
public class CreditCallInfo {
    private Long id;
    String infoMsisdn;
    Integer rateType;
    CreditInfo creditInfo;

    public CreditCallInfo(Long id, String infoMsisdn, Integer rateType, CreditInfo creditInfo) {
        this.id = id;
        this.infoMsisdn = infoMsisdn;
        this.rateType = rateType;
        this.creditInfo = new CreditInfo(creditInfo);
    }
}
