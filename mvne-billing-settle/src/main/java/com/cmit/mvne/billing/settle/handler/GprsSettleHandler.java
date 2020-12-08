package com.cmit.mvne.billing.settle.handler;

import com.cmit.mvne.billing.settle.common.ReprocessStatus;
import com.cmit.mvne.billing.settle.common.SettleErrorCode;
import com.cmit.mvne.billing.settle.common.SettleItem;
import com.cmit.mvne.billing.settle.entity.CdrGprsSettle;
import com.cmit.mvne.billing.settle.entity.SettleCdrGprs;
import com.cmit.mvne.billing.settle.entity.SettleCdrGprsError;
import com.cmit.mvne.billing.settle.service.GprsSettleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * 流量话单结算处理类
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/2/17
 */
@Slf4j
public class GprsSettleHandler {
    @Autowired
    private GprsSettleService gprsSettleService;

    public List<CdrGprsSettle> handleCdr(List<CdrGprsSettle> gprsCdrList) throws Exception {
        log.info("starting handle gprs cdrs, number: {}", gprsCdrList.size());

        List<SettleCdrGprs> settleCdrGprsList = new ArrayList<>();
        List<SettleCdrGprsError> settleCdrGprsErrorList = new ArrayList<>();

        for(CdrGprsSettle gprsCdr : gprsCdrList) {
            log.debug("handle gprs cdr : {}", gprsCdr.toString());

            // 话单检错
            SettleErrorCode result = gprsSettleService.verify(gprsCdr.getMsisdn());

            if (result == null) {
                SettleCdrGprs settleCdrGprs = new SettleCdrGprs();
                BeanUtils.copyProperties(gprsCdr, settleCdrGprs);

                // 设置结算科目
                if ("GP".equals(settleCdrGprs.getRecordType())) {
                    settleCdrGprs.setSettleItem(SettleItem.DOMESTIC_DATA.name());
                } else if ("RGP".equals(settleCdrGprs.getRecordType())) {
                    settleCdrGprs.setSettleItem(SettleItem.INTERNATIONAL_ROAMING_DATA.name());
                }
                // 生成结算账期
                // Siminn 发送文件按时间
                LocalDateTime receiveTime = settleCdrGprs.getReceiveTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                String yyyyMM = gprsSettleService.generateInvoicingPeriod(receiveTime);
                settleCdrGprs.setInvoicingPeriod(yyyyMM);

                settleCdrGprsList.add(settleCdrGprs);
            } else {
                SettleCdrGprsError settleCdrGprsError = new SettleCdrGprsError();
                BeanUtils.copyProperties(gprsCdr, settleCdrGprsError);
                settleCdrGprsError.setStatus(ReprocessStatus.INITIAL);
                settleCdrGprsError.setErrorCode(result.getErrorCode());
                settleCdrGprsError.setErrorMessage(result.getErrorMessage());

                // 设置结算科目
                if ("GP".equals(settleCdrGprsError.getRecordType())) {
                    settleCdrGprsError.setSettleItem(SettleItem.DOMESTIC_DATA.name());
                } else if ("RGP".equals(settleCdrGprsError.getRecordType())) {
                    settleCdrGprsError.setSettleItem(SettleItem.INTERNATIONAL_ROAMING_DATA.name());
                }
                // 生成结算账期
                // Siminn 发送文件按时间
                LocalDateTime receiveTime = settleCdrGprsError.getReceiveTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                String yyyyMM = gprsSettleService.generateInvoicingPeriod(receiveTime);
                settleCdrGprsError.setInvoicingPeriod(yyyyMM);

                settleCdrGprsErrorList.add(settleCdrGprsError);

            }
        }
        gprsSettleService.save(settleCdrGprsList, settleCdrGprsErrorList);
        return gprsCdrList;
    }

    public void handleErrorMessage(MessagingException messagingException) {
//        log.error(messagingException.getFailedMessage().getPayload().toString());
        log.error("MessageException : {}", messagingException.getMessage(), messagingException);

    }
}
