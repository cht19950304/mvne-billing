package com.cmit.mvne.billing.infomanage.remote.service.Impl;

import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.common.MvneInfoManageResponse;
import com.cmit.mvne.billing.infomanage.util.StringUtils;
import com.cmit.mvne.billing.user.analysis.entity.*;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.infomanage.remote.CreditControlClient;
import com.cmit.mvne.billing.infomanage.remote.entity.SmsGatewayDto;
import com.cmit.mvne.billing.infomanage.remote.service.CreditControlService;
import com.cmit.mvne.billing.user.analysis.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.cmit.mvne.billing.infomanage.common.InfoManageErrorConstant.*;
import static com.cmit.mvne.billing.infomanage.common.InfoManageErrorConstant.MYSQL_INSERT_FAILED_CODE;

@Service
@Slf4j
public class CreditControlServiceImpl  implements CreditControlService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private CreditControlClient creditControlClient;
    @Autowired
    private PmProductService pmProductService;
    @Autowired
    private FreeResService freeResService;
    @Autowired
    private CmUserDetailService cmUserDetailService;
    @Autowired
    private ApsBalanceFeeService apsBalanceFeeService;
    //余额阀值，目前暂定2欧元
    @Value(value = "${feeThreshold}")
    private BigDecimal feeThreshold;

    @Override
    public void CreditControlChargeSms(IOrdOrder iOrdOrder) throws MvneException {
        //用户充值之后 调用信控接口发充值信息，若低于阀值，也要发短信。如果用户处于欠费停机状态，当用户充值，余额大于0，触发信控复机
        try{
            //查询用户资料
            List<CmUserDetail> cmUserDetailList1 = cmUserDetailService.findAllByMsisdn(iOrdOrder.getMsisdn());
            //获取最新生效的用户资料
            CmUserDetail cmUserDetail = getValidCmUserDetail(cmUserDetailList1);
            log.info("CreditControlServiceImpl-CreditControlSendSms cmUserDetail is {}",cmUserDetail);
            if ( cmUserDetail == null ){
                log.error("CreditControlServiceImpl-CreditControlSendSms cmUserDetail is null !" );
                throw new MvneException(MYSQL_SELECT_FAILED_CODE,"CreditControlServiceImpl-CreditControlSendSms cmUserDetail is null !");
            }
            //获取余额信息
            ApsBalanceFee apsBalanceFee = apsBalanceFeeService.selectByUserId(iOrdOrder.getUserId());
            log.info("CreditControlServiceImpl-CreditControlSendSms apsBalanceFee is {}",apsBalanceFee);
            if ( apsBalanceFee == null ){
                log.error("CreditControlServiceImpl-CreditControlSendSms apsBalanceFee is null !" );
                throw new MvneException(MYSQL_SELECT_FAILED_CODE,"CreditControlServiceImpl-CreditControlSendSms apsBalanceFee is null !");
            }
            //最新余额，单位为欧分，需转换为欧元
            BigDecimal balanceFeeF = apsBalanceFee.getRemainFee();
            BigDecimal balanceFeeY = balanceFeeF.divide(new BigDecimal(100));
            log.info("CreditControlServiceImpl-CreditControlSendSms balanceFeeF is {} , balanceFeeY is {}",balanceFeeF,balanceFeeY);
            //余额阀值,目前暂定2欧元
            //BigDecimal feeThreshold = new BigDecimal(feeThresholdS);
            log.info("CreditControlServiceImpl-CreditControlSendSms user balanceFeeY is {} , feeThreshold is {}",balanceFeeY,feeThreshold);
            log.info("CreditControlServiceImpl-CreditControlSendSms send number is {} ,Recharge amount is {} , balanceFee is {} , userDetail is {} ,expireDate is {}",iOrdOrder.getMsisdn(),iOrdOrder.getFactMoney(),balanceFeeY,cmUserDetail.getUserStatus(),cmUserDetail.getExpireDate());

/*        if ( userStatus.equals("03") && expireDate.after(new Date()) && balanceFeeY.compareTo(feeThreshold) == -1)
        {
            //发送余额低于阀值信息
            String sendText1= "Dear user, the balance of your number is currently too low, and there is a risk of arrears. It may be shut down. Please recharge it in time. Current balance: "+ balanceFeeY+" euros, below current balance threshold: "+ feeThreshold+" euros";
            sendSmsGateway(iOrdOrder.getMsisdn(),sendText1,"0","1");
            log.info("CreditControlServiceImpl-CreditControlSendSms sendSmsGateway sendText1 is {} , operation is {} , reason is {}",sendText1,0,1);

        }*/
            if ( cmUserDetail.getUserStatus().equals("03") && cmUserDetail.getExpireDate().after(new Date()) && balanceFeeY.compareTo(feeThreshold) == 1){
                //发送充值信息
                String sendText2 = "Dear user, your number has been recharged successfully . Recharge amount is " + iOrdOrder.getFactMoney()+" euros , "+"Current balance: "+balanceFeeY+" euros.";
                sendSmsGateway(iOrdOrder.getMsisdn(),sendText2,"0","21");
                log.info("CreditControlServiceImpl-CreditControlSendSms sendSmsGateway sendText2 is {} , operation is {} , reason is {}",sendText2,0,21);
            }
            if ( cmUserDetail.getUserStatus().equals("02") && cmUserDetail.getExpireDate().after(new Date()) && balanceFeeY.compareTo(feeThreshold) == 1 )
            {
                //复机的条件，1：停机状态；2、话费余额高于阈值
                //发送复机信息
                String sendText3 = "Dear user, your number has been restored . Recharge amount is " + iOrdOrder.getFactMoney()+" euros , Current balance: "+balanceFeeY+" euros.";
                sendSmsGateway(iOrdOrder.getMsisdn(),sendText3,"2","21");
                log.info("CreditControlServiceImpl-CreditControlSendSms sendSmsGateway sendText3 is {} , operation is {} , reason is {}",sendText3,2,21);

            }
        }catch (Exception e){
            if ( e instanceof MvneException ){
                throw new MvneException(((MvneException) e).getErrCode(),((MvneException) e).getErrDesc());
            } else {
                throw new MvneException(SENT_SMS_CREDIT_FAILED_CODE,StringUtils.getExceptionText(e).substring(0, 1023));
            }
        }


    }
    private CmUserDetail getValidCmUserDetail(List<CmUserDetail> cmUserDetailList){
        for ( CmUserDetail cmUserDetail : cmUserDetailList){
            if ( cmUserDetail.getExpireDate().after(new Date())){
                return cmUserDetail;
            }else {
                continue;
            }
        }
        return null;
    }
    @Override
    public void CreditControlOfferSms(IOrdOrder iOrdOrder, IProd iProd) throws MvneException{
        try{
            //获得产品费用
            BigDecimal productFee = iProd.getProductFee();
            //获得产品名
            String productName = pmProductService.selectByProductId(iProd.getProductId());
            //获取产品资源
            BigDecimal amountGb = null;
            List<FreeRes> freeResList = freeResService.selectProduct(iProd.getProductId());
            if ( freeResList.size() > 0 ){
                for (FreeRes freeRes : freeResList){
                    BigDecimal measureId = freeRes.getMeasureId();
                    //目前只返回流量资源
                    if ( measureId.compareTo(new BigDecimal(103)) == 0 )
                    {
                        BigDecimal amountB = freeRes.getAmount();
                        amountGb = amountB.divide(new BigDecimal(1073741824),2,BigDecimal.ROUND_HALF_UP);
                    }
                }
            }else {
                log.error("CreditControlServiceImpl-CreditControlOfferSms select freeRes is null !");
                throw new MvneException(MYSQL_SELECT_FAILED_CODE,"CreditControlServiceImpl-CreditControlOfferSms select freeRes is null !");
            }

            log.info("CreditControlServiceImpl-CreditControlOfferSms productFee is {} , productName is {} , amountGb is {}",productFee,productName,amountGb);
            String sendText1 = "Dear user, you have successfully ordered "+ productName +" product, the product cost is " + productFee +" euros , and the product resource is "+ amountGb +"G .";
            //发送复机请求给信控
            sendSmsGateway(iOrdOrder.getMsisdn(),sendText1,"0","32");
            log.info("CreditControlServiceImpl-CreditControlOfferSms sendSmsGateway sendText1 is {} , operation is {} , reason is {}",sendText1,0,32);

            //如果余额低于阀值调用信控接口发送提醒短信
            //获取余额信息
            ApsBalanceFee apsBalanceFee = apsBalanceFeeService.selectByUserId(iOrdOrder.getUserId());
            log.info("CreditControlServiceImpl-CreditControlOfferSms apsBalanceFee is {}",apsBalanceFee);
            if ( apsBalanceFee == null){
                log.error("CreditControlServiceImpl-CreditControlSendSms apsBalanceFee is null !" );
                throw new MvneException(MYSQL_SELECT_FAILED_CODE,"CreditControlServiceImpl-CreditControlOfferSms apsBalanceFee is null !");
            }
            //最新余额，单位为欧分，需转换为欧元
            BigDecimal balanceFeeF = apsBalanceFee.getRemainFee();
            BigDecimal balanceFeeY = balanceFeeF.divide(new BigDecimal(100));
            log.info("CreditControlServiceImpl-CreditControlOfferSms balanceFeeF is {} , balanceFeeY is {}",balanceFeeF,balanceFeeY);
            //余额阀值,目前暂定2欧元
            //BigDecimal feeThreshold = new BigDecimal(2);
            log.info("CreditControlServiceImpl-CreditControlOfferSms user balanceFeeY is {} , feeThreshold is {}",balanceFeeY,feeThreshold);
            log.info("CreditControlServiceImpl-CreditControlOfferSms send number is {} ,productFee is {} , balanceFee is {} euros",iOrdOrder.getMsisdn(),iProd.getProductFee(),balanceFeeY);
            if ( balanceFeeY.compareTo(feeThreshold) == -1)
            {
                //发送余额低于阀值信息给信控
                String sendText2= "Dear user, the balance of your number is currently too low, and there is a risk of arrears. It may be shut down. Please recharge it in time. Current balance: "+ balanceFeeY+" euros, below current balance threshold: "+ feeThreshold+" euros";
                sendSmsGateway(iOrdOrder.getMsisdn(),sendText2,"0","1");
                log.info("CreditControlServiceImpl-CreditControlSendSms sendSmsGateway sendText2 is {} , operation is {} , reason is {}",sendText2,0,1);
            }
        }catch (Exception e){
            if ( e instanceof MvneException ){
                throw new MvneException(((MvneException) e).getErrCode(),((MvneException) e).getErrDesc());
            } else {
                throw new MvneException(SENT_SMS_CREDIT_FAILED_CODE,StringUtils.getExceptionText(e).substring(0, 1023));
            }
        }

    }

    @Override
    public void CreditControlChangeCardSms(IOrdOrder iOrdOrder, IUser iUser) throws MvneException {
        try{

            //获取余额信息
            ApsBalanceFee apsBalanceFee = apsBalanceFeeService.selectByUserId(iOrdOrder.getUserId());
            log.info("CreditControlServiceImpl-CreditControlChangeCardSms apsBalanceFee is {}",apsBalanceFee);
            if ( apsBalanceFee == null ){
                log.error("CreditControlServiceImpl-CreditControlChangeCardSms apsBalanceFee is null !" );
                throw new MvneException(MYSQL_SELECT_FAILED_CODE,"CreditControlServiceImpl-CreditControlChangeCardSms apsBalanceFee is null !");
            }
            //最新余额，单位为欧分，需转换为欧元
            BigDecimal balanceFeeF = apsBalanceFee.getRemainFee();
            BigDecimal balanceFeeY = balanceFeeF.divide(new BigDecimal(100));
            log.info("CreditControlServiceImpl-CreditControlChangeCardSms balanceFeeF is {} , balanceFeeY is {}",balanceFeeF,balanceFeeY);

            log.info("CreditControlServiceImpl-CreditControlChangeCardSms user balanceFeeY is {} ",balanceFeeY);
            log.info("CreditControlServiceImpl-CreditControlChangeCardSms send number is {} , balanceFee is {} , userDetail is {} ",iOrdOrder.getMsisdn(),balanceFeeY,iUser.getUserStatus());

            if ( iUser.getUserStatus().equals("03")  && balanceFeeY.compareTo(new BigDecimal(0)) == 0){
                //如果为匿名补换卡，余额为0，发送停机信息
                String sendText2 = "Dear user, your number has been changecard successfully, but the balance of your number is currently too low , "+"Current balance: "+balanceFeeY+" euros.";
                sendSmsGateway(iOrdOrder.getMsisdn(),sendText2,"1","3");
                log.info("CreditControlServiceImpl-CreditControlChangeCardSms sendSmsGateway sendText2 is {} , operation is {} , reason is {}",sendText2,1,3);
            }
        }catch (Exception e){
            if ( e instanceof MvneException ){
                throw new MvneException(((MvneException) e).getErrCode(),((MvneException) e).getErrDesc());
            } else {
                throw new MvneException(SENT_SMS_CREDIT_FAILED_CODE,StringUtils.getExceptionText(e).substring(0, 1023));
            }
        }
    }

    private void sendSmsGateway(String msisdn, String sendText, String operation,String reason) throws MvneException
    {

        log.info("CreditControlServiceImpl-sendSmsGateway send credit control start! number is : {} ",msisdn);
        SmsGatewayDto smsGatewayDto = new SmsGatewayDto();
        smsGatewayDto.setMsisdn(msisdn);
        smsGatewayDto.setText(sendText);
        smsGatewayDto.setOperation(operation);
        smsGatewayDto.setReason(reason);
        MvneInfoManageResponse mvneInfoManageResponse = creditControlClient.userStart(smsGatewayDto);
        log.info("CreditControlServiceImpl-sendSmsGateway send credit control send smsGatewayDto is {}",smsGatewayDto);
        log.info("CreditControlServiceImpl-sendSmsGateway send credit control start response : {}",mvneInfoManageResponse);
        log.info("mvneInfoManageResponse code is {} ",mvneInfoManageResponse.get("code"));
        if ( mvneInfoManageResponse.get("code").equals(200)){
            log.info("CreditControlServiceImpl-sendSmsGateway send credit control success !");
        }else {
            log.info("CreditControlServiceImpl-sendSmsGateway send credit control failed !");
            throw new MvneException(SENT_SMS_CREDIT_FAILED_CODE,mvneInfoManageResponse.get("message").toString());
        }
    }
}
