package com.cmit.mvne.billing.settle.service;

import com.cmit.mvne.billing.settle.common.SettleItem;
import com.cmit.mvne.billing.settle.common.SysMeasure;
import com.cmit.mvne.billing.settle.dao.SettleSiminnRuleMapper;
import com.cmit.mvne.billing.settle.entity.SettleSiminnRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/27
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SettleSiminnRuleServiceTest {

    @Autowired
    private SettleSiminnRuleService settleSiminnRuleService;

    @Test
    public void testSaveAll() {
        List<SettleSiminnRule> settleSiminnRuleList = new ArrayList<>();
//        // GprsNative
        SettleSiminnRule siminnRuleGprsNative = new SettleSiminnRule();
        siminnRuleGprsNative.setItemName(SettleItem.DOMESTIC_DATA.name());
        siminnRuleGprsNative.setItemMeasure(SysMeasure.KB.name());
        siminnRuleGprsNative.setChargeFee(new BigDecimal(10));
        siminnRuleGprsNative.setChargeMeasure(SysMeasure.EuroCent.name());
        siminnRuleGprsNative.setEffectiveTime(new Date());
        LocalDateTime localDateTime = LocalDateTime.of(2099, 12, 31, 23, 59, 59);
        Date end = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        siminnRuleGprsNative.setExpireTime(end);
        settleSiminnRuleList.add(siminnRuleGprsNative);
//
        // GprsNative
        SettleSiminnRule siminnRuleGprsRoaming = new SettleSiminnRule();
        siminnRuleGprsRoaming.setItemName(SettleItem.INTERNATIONAL_ROAMING_DATA.name());
        siminnRuleGprsRoaming.setItemMeasure(SysMeasure.KB.name());
        siminnRuleGprsRoaming.setChargeFee(new BigDecimal(20));
        siminnRuleGprsRoaming.setChargeMeasure(SysMeasure.EuroCent.name());
        siminnRuleGprsRoaming.setEffectiveTime(new Date());
//        LocalDateTime localDateTime = LocalDateTime.of(2099, 12, 31, 23, 59, 59);
//        Date end = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        siminnRuleGprsRoaming.setExpireTime(end);
        settleSiminnRuleList.add(siminnRuleGprsRoaming);
//
//        // GprsNative
        SettleSiminnRule siminnRuleGsmNative = new SettleSiminnRule();
        siminnRuleGsmNative.setItemName(SettleItem.DOMESTIC_VOICE.name());
        siminnRuleGsmNative.setItemMeasure(SysMeasure.Minute.name());
        siminnRuleGsmNative.setChargeFee(new BigDecimal(10));
        siminnRuleGsmNative.setChargeMeasure(SysMeasure.EuroCent.name());
        siminnRuleGsmNative.setEffectiveTime(new Date());
//        LocalDateTime localDateTime = LocalDateTime.of(2099, 12, 31, 23, 59, 59);
//        Date end = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        siminnRuleGsmNative.setExpireTime(end);
        settleSiminnRuleList.add(siminnRuleGsmNative);
//
//        // GprsNative
        SettleSiminnRule siminnRuleGsmRoaming = new SettleSiminnRule();
        siminnRuleGsmRoaming.setItemName(SettleItem.INTERNATIONAL_ROAMING_VOICE.name());
        siminnRuleGsmRoaming.setItemMeasure(SysMeasure.Minute.name());
        siminnRuleGsmRoaming.setChargeFee(new BigDecimal(10));
        siminnRuleGsmRoaming.setChargeMeasure(SysMeasure.EuroCent.name());
        siminnRuleGsmRoaming.setEffectiveTime(new Date());
//        LocalDateTime localDateTime = LocalDateTime.of(2099, 12, 31, 23, 59, 59);
//        Date end = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        siminnRuleGsmRoaming.setExpireTime(end);
        settleSiminnRuleList.add(siminnRuleGsmRoaming);
//
//
        SettleSiminnRule siminnRuleSmsNative = new SettleSiminnRule();
        siminnRuleSmsNative.setItemName(SettleItem.DOMESTIC_SMS.name());
        siminnRuleSmsNative.setItemMeasure(SysMeasure.Message.name());
        siminnRuleSmsNative.setChargeFee(new BigDecimal(10));
        siminnRuleSmsNative.setChargeMeasure(SysMeasure.EuroCent.name());
        siminnRuleSmsNative.setEffectiveTime(new Date());
//        LocalDateTime localDateTime = LocalDateTime.of(2099, 12, 31, 23, 59, 59);
//        Date end = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        siminnRuleSmsNative.setExpireTime(end);
        settleSiminnRuleList.add(siminnRuleSmsNative);
//
        SettleSiminnRule siminnRuleSmsRoaming = new SettleSiminnRule();
        siminnRuleSmsRoaming.setItemName(SettleItem.INTERNATIONAL_ROAMING_SMS.name());
        siminnRuleSmsRoaming.setItemMeasure(SysMeasure.Message.name());
        siminnRuleSmsRoaming.setChargeFee(new BigDecimal(10));
        siminnRuleSmsRoaming.setChargeMeasure(SysMeasure.EuroCent.name());
        siminnRuleSmsRoaming.setEffectiveTime(new Date());
//        LocalDateTime localDateTime = LocalDateTime.of(2099, 12, 31, 23, 59, 59);
//        Date end = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        siminnRuleSmsRoaming.setExpireTime(end);
        settleSiminnRuleList.add(siminnRuleSmsRoaming);

        SettleSiminnRule siminnRulePhoneActivation = new SettleSiminnRule();
        siminnRulePhoneActivation.setItemName(SettleItem.OPEN_ACCOUNT_ACTIVATION.name());
        siminnRulePhoneActivation.setItemMeasure(SysMeasure.Piece.name());
        siminnRulePhoneActivation.setChargeFee(new BigDecimal(100));
        siminnRulePhoneActivation.setChargeMeasure(SysMeasure.EuroCent.name());
        siminnRulePhoneActivation.setEffectiveTime(new Date());
//        LocalDateTime localDateTime = LocalDateTime.of(2099, 12, 31, 23, 59, 59);
//        Date end = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        siminnRulePhoneActivation.setExpireTime(end);
        settleSiminnRuleList.add(siminnRulePhoneActivation);

//        for (SettleSiminnRule rule : settleSiminnRuleList) {
//            settleSiminnRuleService.save(rule);
//        }
//        for (SettleSiminnRule rule: settleSiminnRuleList) {
//            settleSiminnRuleMapper.insert(rule);
//        }

        settleSiminnRuleService.saveBatch(settleSiminnRuleList);

//        // GprsNative
//        SettleSiminnRule siminnRuleGprsNative = new SettleSiminnRule();
//        siminnRuleGprsNative.setItemName(SettleItem.GPRS_NATIVE);
//        siminnRuleGprsNative.setChargeFee(new BigDecimal(10));
//        siminnRuleGprsNative.setEffectiveTime(new Date());
//        LocalDateTime localDateTime = LocalDateTime.of(2099, 12, 31, 23, 59, 59);
//        Date end = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
//        siminnRuleGprsNative.setExpireTime(end);
//        siminnRuleGprsNative.setMeasureId(SysMeasure.EuroCent.name());

    }

}