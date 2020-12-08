package com.cmit.mvne.billing.settle.service;

import com.cmit.mvne.billing.settle.common.SettleErrorCode;
import com.cmit.mvne.billing.settle.entity.CdrGprsSettle;
import com.cmit.mvne.billing.settle.entity.SettleCdrGprs;
import com.cmit.mvne.billing.settle.entity.SettleCdrGprsError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Gprs 结算业务处理类
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/7
 */
public interface GprsSettleService {

    SettleErrorCode verify(String msisdn);

    void save(List<SettleCdrGprs> gprsList, List<SettleCdrGprsError> gprsErrorList);

    String generateInvoicingPeriod(LocalDateTime receiveTime);
}
