package com.cmit.mvne.billing.rating.gprs.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/6/3 15:26
 */

@Data
@JSONType(orders={"tableDate","createTimeS","createTimeE","finishTimeS","finishTimeE","msisdn","productId","itemId","reratStatus","ids","idList","page","size"})
public class ReratDto {
    @NotNull
    @NotBlank
    String tableDate;

    Long createTimeS;

    Long createTimeE;

    Long finishTimeS;

    Long finishTimeE;

    String msisdn;

    Long productId;

    Long itemId;

    String reratStatus;

    String ids;

    List<Long> idList;

    int page;

    int size;

    public String toJSONString() {
        return JSON.toJSONString(this);
    }
}
