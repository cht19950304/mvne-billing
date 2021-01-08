package com.cmit.mvne.billing.rating.gprs.remote;

import com.cmit.mvne.billing.user.analysis.entity.FreeRes;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/2/26 14:34
 */
@Data
public class Resource {
    String resourceType;

    HashMap<String, BigDecimal> resourceMap = new HashMap<>();

    public Resource(BigDecimal valueAmount, BigDecimal valueMeasure) {
        resourceType = "GPRS";

        resourceMap.put("valueAmount", valueAmount);
        resourceMap.put("valueMeasure", valueMeasure);
    }

    public Resource(BigDecimal feeAmount, BigDecimal feeMeasure, BigDecimal valueAmount, BigDecimal valueMeasure) {
        resourceType = "GPRS";

        resourceMap.put("feeAmount", feeAmount);
        resourceMap.put("feeMeasure", feeMeasure);
        resourceMap.put("valueAmount", valueAmount);
        resourceMap.put("valueMeasure", valueMeasure);
    }
}
