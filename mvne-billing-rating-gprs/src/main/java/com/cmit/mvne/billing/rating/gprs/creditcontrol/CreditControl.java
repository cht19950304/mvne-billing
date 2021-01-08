package com.cmit.mvne.billing.rating.gprs.creditcontrol;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cmit.mvne.billing.rating.gprs.common.CreditCallInfo;
import com.cmit.mvne.billing.rating.gprs.common.Measures;
import com.cmit.mvne.billing.rating.gprs.config.SystemConfiguration;
import com.cmit.mvne.billing.rating.gprs.remote.service.CreditControlService;
import com.cmit.mvne.billing.rating.gprs.util.MeasureExchangeUtils;
import com.cmit.mvne.billing.user.analysis.common.MvneException;
import com.cmit.mvne.billing.user.analysis.entity.SysMeasureUnitExchange;
import com.cmit.mvne.billing.user.analysis.entity.SysSmsModel;
import com.cmit.mvne.billing.user.analysis.service.SysSmsModelService;
import com.cmit.mvne.billing.user.analysis.util.ObjectUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.cmit.mvne.billing.rating.gprs.common.RateTypeConstant.*;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/2/21 8:57
 */
@Data
@Component
@Slf4j
public class CreditControl {
    @Autowired
    CreditControlService creditControlService;
    @Autowired
    SystemConfiguration systemConfiguration;
    @Autowired
    SysSmsModelService sysSmsModelService;

    public void callList(List<CreditCallInfo> creditCallInfoList) throws MvneException {
        List<SmsGatewayDto> smsGatewayDtoList = new ArrayList<>();
        for (CreditCallInfo creditCallInfo : creditCallInfoList) {
            List<SmsGatewayDto> smsGatewayDtos = call(creditCallInfo.getId(), creditCallInfo.getInfoMsisdn(), creditCallInfo.getRateType(), creditCallInfo.getCreditInfo());
            smsGatewayDtoList.addAll(smsGatewayDtos);
        }
        creditControlService.smsList(smsGatewayDtoList);
    }

    /**
     * 调用信控，不需要等待处理结果，只需要调用成功即可，否则会影响计费性能
     * @param infoMsisdn
     * @param rateType
     * @throws MvneException
     */
    public List<SmsGatewayDto> call(Long id, String infoMsisdn, Integer rateType, CreditInfo creditInfo) throws MvneException {
        BigDecimal outOfBalance = new BigDecimal(0);
        BigDecimal limitedValue = systemConfiguration.getLimitedValue();
        BigDecimal limitedFee = systemConfiguration.getLimitedFee();

        BigDecimal leftFreeRes = creditInfo.getLeftFreeRes();
        BigDecimal balance = creditInfo.getBalance();

        // 注意余额和余量不会同时为null

        log.info("Credit cdr id:{}", id);
        List<SmsGatewayDto> smsGatewayDtoList = new ArrayList<>();
        // 根据信控类型
        // 如果剩余免费资源低于阈值
        if (null != leftFreeRes) {
            // 从Byte转成MB，向下取整，limit则unnecessary
            BigDecimal leftFreeResMB = MeasureExchangeUtils.exchangeFloor(leftFreeRes, Measures.B, Measures.MB, 2);
            BigDecimal limitedValueMB = MeasureExchangeUtils.exchangeFloor(limitedValue, Measures.B, Measures.MB, 2);

            // 因为非applied情况也会set free res，所以判断一下
            if (rateType.equals(APPLIED_TYPE)) {
                if (leftFreeRes.compareTo(limitedValue) == -1) {
                    // 资源不足提醒
                    String operation = CreditControlOperation.NO_OPERATION;
                    String reason = CreditControlReason.LOW_GPRS_FREERES_REASON;
                    String text = getText(operation, reason);
                    text = text.replace("XXX_CURRENT_FREERES", leftFreeResMB.toString());
                    text = text.replace("XXX_LIMIT_FREERES", limitedValueMB.toString());
                    SmsGatewayDto smsGatewayDto = new SmsGatewayDto(infoMsisdn, text, operation, reason);
                    log.info(smsGatewayDto.toJsonString());
                    /*try {
                        creditControlService.sms(smsGatewayDto);
                    } catch (Exception e) {
                        log.error("Call credit info fail!", e);
                    }*/
                    smsGatewayDtoList.add(smsGatewayDto);
                }
            }
        }

        if (null != balance) {
            // 转成欧元，unnecessary
            BigDecimal balanceEuros = MeasureExchangeUtils.exchangeFloor(balance, Measures.EURO_CENT, Measures.EURO, 2);
            BigDecimal limitedFeeEuros = MeasureExchangeUtils.exchangeFloor(limitedFee, Measures.EURO_CENT, Measures.EURO, 2);
            BigDecimal shouldTopEuros = limitedFeeEuros.subtract(balanceEuros);

            // 如果当前余额小于阈值
            if (balance.compareTo(limitedFee) == -1) {
                // 如果欠费，一定停机
                if (balance.compareTo(outOfBalance) == -1) {
                    String operation = CreditControlOperation.STOP_OPERATION;
                    String reason = CreditControlReason.OUT_OF_BALANCE_REASON;
                    String text = getText(operation, reason);
                    text = text.replace("XXX_CURRENT_BALANCE", balanceEuros.toString());
                    text = text.replace("XXX_TOPUP_BALANCE", shouldTopEuros.toString());
                    SmsGatewayDto smsGatewayDto = new SmsGatewayDto(infoMsisdn, text, operation, reason);
                    // 停机
                    log.info(smsGatewayDto.toJsonString());
                    /*try {
                        creditControlService.sms(smsGatewayDto);
                    } catch (Exception e) {
                        log.error("Call credit info fail!", e);
                    }*/
                    smsGatewayDtoList.add(smsGatewayDto);
                } else if (rateType.equals(NOT_APPLIED_TYPE) || rateType.equals(APPLIED_TYPE) || rateType.equals(APPLIED_BUT_OVER_TYPE)) {
                    // 低阈值但是未欠费，并且是有订购套餐的，则必有免费资源
                    if (leftFreeRes.compareTo(new BigDecimal(0)) == 0) {
                        // 如果免费资源为0，停机
                        String operation = CreditControlOperation.STOP_OPERATION;
                        String reason = CreditControlReason.LOW_BALANCE_AND_ZERO_FREERES_REASON;
                        String text = getText(operation, reason);
                        text = text.replace("XXX_CURRENT_BALANCE", balanceEuros.toString());
                        text = text.replace("XXX_LIMIT_BALANCE", limitedFeeEuros.toString());
                        SmsGatewayDto smsGatewayDto = new SmsGatewayDto(infoMsisdn, text, operation, reason);
                        log.info(smsGatewayDto.toJsonString());
                        /*try {
                            creditControlService.sms(smsGatewayDto);
                        } catch (Exception e) {
                            log.error("Call credit info fail!", e);
                        }*/
                        smsGatewayDtoList.add(smsGatewayDto);
                    } else {
                        // 免费资源不为0，只提示低余额
                        String operation = CreditControlOperation.NO_OPERATION;
                        String reason = CreditControlReason.LOW_BALANCE_REASON;
                        String text = getText(operation, reason);
                        text = text.replace("XXX_CURRENT_BALANCE", balanceEuros.toString());
                        text = text.replace("XXX_LIMIT_BALANCE", limitedFeeEuros.toString());
                        SmsGatewayDto smsGatewayDto = new SmsGatewayDto(infoMsisdn, text, operation, reason);
                        log.info(smsGatewayDto.toJsonString());
                        /*try {
                            creditControlService.sms(smsGatewayDto);
                        } catch (Exception e) {
                            log.error("Call credit info fail!", e);
                        }*/
                        smsGatewayDtoList.add(smsGatewayDto);
                    }
                } else {
                    // 没有订购套餐的，低余额必停机
                    // 没有免费资源，也认为是免费资源为0
                    String operation = CreditControlOperation.STOP_OPERATION;
                    String reason = CreditControlReason.LOW_BALANCE_AND_ZERO_FREERES_REASON;
                    String text = getText(operation, reason);
                    text = text.replace("XXX_CURRENT_BALANCE", balanceEuros.toString());
                    text = text.replace("XXX_LIMIT_BALANCE", limitedFeeEuros.toString());
                    SmsGatewayDto smsGatewayDto = new SmsGatewayDto(infoMsisdn, text, operation, reason);
                    // 低余额提醒
                    log.info(smsGatewayDto.toJsonString());
                    /*try {
                        creditControlService.sms(smsGatewayDto);
                    } catch (Exception e) {
                        log.error("Call credit info fail!", e);
                    }*/
                    smsGatewayDtoList.add(smsGatewayDto);
                }
            }
        }

        // 根据上述参数，调用信控接口
        return smsGatewayDtoList;
    }

    public String getText(String operation, String reason) {
        // 根据参数从加载的短信模板中选择

        //log.info(lambdaQueryWrapper.getSqlSegment());
        SysSmsModel sysSmsModel = sysSmsModelService.selectByReasonAndOperation(reason, operation);
        if (sysSmsModel != null) {
            return sysSmsModel.getText();
        }

        return null;
    }
}
