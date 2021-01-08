package com.cmit.mvne.billing.rating.gprs.dto;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/6/4 10:20
 */

@Data
public class RedoDto {
    String msisdn;

    @NotNull
    Long finishTimeS;

    @NotNull
    Long finishTimeE;

    String originalFile;

    String errCode;

    String redoFlag;

    String tableIds;

    List<Long> tableIdList;

    int page;

    int size;

    public String toJSONString() {
        return JSON.toJSONString(this);
    }
}
