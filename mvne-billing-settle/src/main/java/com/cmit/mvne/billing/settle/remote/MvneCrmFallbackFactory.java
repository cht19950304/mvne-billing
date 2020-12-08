package com.cmit.mvne.billing.settle.remote;

import com.cmit.mvne.billing.settle.common.MvneCrmResponse;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/25
 */
@Slf4j
@Component
public class MvneCrmFallbackFactory implements FallbackFactory<MvneCrmClient> {
    @Override
    public MvneCrmClient create(Throwable e) {
        return new MvneCrmClient() {
            @Override
            public MvneCrmResponse queryCreateUserOrdersByMonth(String billMonth) {
                log.error("Invoke mvne-crm interface queryCreateUserOrders failed!", e);
                return new MvneCrmResponse().fail().message("Invoke mvne-crm interface queryCreateUserOrders failed!");
            }

            @Override
            public MvneCrmResponse queryCreateUserOrdersByDay(String billDay) {
                log.error("Invoke mvne-crm interface queryCreateUserOrders failed!", e);
                return new MvneCrmResponse().fail().message("Invoke mvne-crm interface queryCreateUserOrders failed!");
            }
        };
    }
}
