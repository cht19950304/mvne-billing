package com.cmit.mvne.billing.rating.gprs.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/2/25 18:32
 */
@FeignClient(value = "mvne-info-managerrr", url = "http://296o51265u.qicp.vip/test")
public interface TestClient {
    @PostMapping(value = "/iii")
    String test();
}
