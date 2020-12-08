package com.cmit.mvne.billing.settle.remote;

import com.cmit.mvne.billing.settle.common.MvneCrmResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/25
 */
@FeignClient(value = "mvne-crm-core", fallbackFactory = MvneCrmFallbackFactory.class)
public interface MvneCrmClient {

    @GetMapping(value = "/order/orderInfo/createUser/{billMonth}")
    MvneCrmResponse queryCreateUserOrdersByMonth(@PathVariable("billMonth") String billMonth);

    @GetMapping(value = "/order/orderInfo/createUserForDate/{billDay}")
    MvneCrmResponse queryCreateUserOrdersByDay(@PathVariable("billDay") String billDay);


}
