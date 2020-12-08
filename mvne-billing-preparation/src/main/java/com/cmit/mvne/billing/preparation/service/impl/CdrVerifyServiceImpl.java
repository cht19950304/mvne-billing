package com.cmit.mvne.billing.preparation.service.impl;

import com.cmit.mvne.billing.preparation.entity.BdOperatorCode;
import com.cmit.mvne.billing.preparation.service.BdOperatorCodeService;
import com.cmit.mvne.billing.preparation.service.CdrVerifyService;
import com.cmit.mvne.billing.preparation.util.DateTimeUtil;
import com.cmit.mvne.billing.preparation.util.RegexPatternUtil;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 话单级校验
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/4/3
 */
@Service
@Slf4j
public class CdrVerifyServiceImpl implements CdrVerifyService {

    @Autowired
    private BdOperatorCodeService bdOperatorCodeService;

    private static Set<String>  nulliSet;

    static {
        nulliSet = Sets.newHashSet("+0000","+0100","+0200","+0300","+0400","+0500","+0600","+0700","+0800","+0900","+0015","+0115","+0215","+0315","+0415","+0515","+0615","+0715","+0815","+0915","+0030","+0130","+0230","+0330","+0430","+0530","+0630","+0730","+0830","+0930","+0045","+0145","+0245","+0345","+0445","+0545","+0645","+0745","+0845","+0945","+1000","+1100","+1200","+1015","+1115","+1215","+1030","+1130","+1230","+1045","+1145","+1245","+1300",
                "-0000","-0100","-0200","-0300","-0400","-0500","-0600","-0700","-0800","-0900","-0015","-0115","-0215","-0315","-0415","-0515","-0615","-0715","-0815","-0915","-0030","-0130","-0230","-0330","-0430","-0530","-0630","-0730","-0830","-0930","-0045","-0145","-0245","-0345","-0445","-0545","-0645","-0745","-0845","-0945","-1000","-1100","-1200","-1015","-1115","-1215","-1030","-1130","-1230","-1045","-1145","-1245","-1300");
    }

    @Override
    public String verify(String[] cdr, Date fileRecieveDate) {

        // 话单长度 不合格
        if (cdr.length != 25) {
            if (cdr.length > 25) {
                return "Severe260";
            }else {
                return "Severe261";
            }
        }

        if(StringUtils.isEmpty(cdr[0]) || !Arrays.asList("RGP", "RSMO", "RMT", "GP", "SMS").contains(cdr[0])) {
            return "Severe10";
        }

        if(StringUtils.isEmpty(cdr[4]) || !RegexPatternUtil.simpleMatch("^354(\\d{7}|\\d{9})$", cdr[4])) {
            return "Severe31";
        }

        if(StringUtils.isEmpty(cdr[5]) || !RegexPatternUtil.simpleMatch("^\\d*$", cdr[5])) {
            return "Severe32";
        }

        String result = verifyDate(cdr[0], cdr[6], fileRecieveDate);
        if (result != null) {
            return result;
        }

        if(StringUtils.isEmpty(cdr[7]) || !isPositiveInteger(cdr[7])) {
            return "Severe22";
        }

        // 只有流量话单需要校验上下发不为正整数
        if(cdr[0].equals("RGP") || cdr[0].equals("GP")) {
            if(!isPositiveBigDecimal(cdr[8])) {
                return "Severe23";
            }
            if(!isPositiveBigDecimal(cdr[9])) {
                return "Severe24";
            }
        } else {
            // 规整，防止异常
            cdr[8] = "0";
            cdr[9] = "0";
        }

        if(cdr[0].equals("RGP") || cdr[0].equals("RSMO") || cdr[0].equals("RMT")) {
            // 校验局数据表
            List<BdOperatorCode> byOperatorCodeList = bdOperatorCodeService.findByOperatorCode(cdr[10]);
            if (byOperatorCodeList == null || byOperatorCodeList.size() < 1) {
                return "Severe40";
            }
        }


        // 只有漫游类需要添加时区
        if(cdr[0].equals("RGP") || cdr[0].equals("RSMO") || cdr[0].equals("RMT")) {
//            if( !RegexPatternUtil.simpleMatch("^(\\+0[0-9]00|\\+0[0-9]15|\\+0[0-9]30|\\+0[0-9]45|\\+1[0-4]00|\\+1[0-4]15|\\+1[0-4]30|-0[1-9]45|-1[0-3]00|-1[0-3]15|-1[0-3]30|-1[0-3]45)$",
//                    cdr[13])) {
//                return "Severe41";
//            }
            // 正则表达式cpu占比过高
            if( !nulliSet.contains(cdr[13])) {
                return "Severe41";
            }
        } else {
            // 如果是本地话单（冰岛），且为空，设置为零时区
//            if (StringUtils.isEmpty(cdr[13])) {
//                cdr[13] = "+0000";
//            }
            cdr[13] = "+0000";
        }


        if( !RegexPatternUtil.simpleMatch("^\\d*$", cdr[24])) {
            return "Severe50";
        }

        return null;
    }

    private boolean isPositiveInteger(String s) {
        try {
            int i = Integer.parseInt(s);
            return i >= 0;
        } catch (NumberFormatException e) {
            log.error("Parse Integer Exception", e);
            return false;
        }
    }

    private boolean isPositiveBigDecimal(String s) {
        try {
            BigDecimal i = new BigDecimal(s);
            return (i.compareTo(new BigDecimal(0)) >= 0)
                    && (new BigDecimal(i.longValue()).compareTo(i) == 0);
        } catch (NumberFormatException e) {
            log.error("Parse BigDecimal Exception : ", e);
            return false;
        } catch (Exception e2) {
            log.error("hello world:", e2);
            return false;
        }
    }

    /**
     * 判断是否有效时间字符串
     * 判断是否过期
     *   国内流量 最晚 ？天
     *   国际漫游流量 最晚 30天
     * @param str 时间字符串
     * @return 错误编码，返回null表明校验通过
     */
    public String verifyDate(String recordType, String str, Date fileReceiveDate) {

        LocalDateTime fileReceiveLocalDate = DateTimeUtil.dateToLocalDateTime(fileReceiveDate);
        if(StringUtils.isEmpty(str)) {
            return "Severe20";
        }
        try {
            LocalDateTime localEventTime = DateTimeUtil.parseByBasicFormater(str);
            if(recordType.equals("RGP") || recordType.equals("RSMO") || recordType.equals("RMT")) {
                if(fileReceiveLocalDate.minusDays(30).isAfter(localEventTime)) {
                    log.debug("CDR is overdue");
                    return "Severe21";
                }
            } else if(recordType.equals("GP") || recordType.equals("SMS")) {
                if(fileReceiveLocalDate.minusDays(15).isAfter(localEventTime)) {
                    log.debug("CDR is overdue");
                    return "Severe21";
                }
            }

            if(fileReceiveLocalDate.plusMinutes(5).isBefore(localEventTime)) {
                log.debug("CDR Time is after now()+5minites!");
                return "Severe25";
            }
            return null;
        } catch (DateTimeParseException e) {
            log.error("DateTime parse Exception!", e);
            return "Severe20";
        }
    }

    public static void main(String[] args) {
//        boolean b = RegexPatternUtil.simpleMatch("^(\\+0[0-9]00|\\+0[0-9]15|\\+0[0-9]30|\\+0[0-9]45|\\+1[0-4]00|\\+1[0-4]15|\\+1[0-4]30|-0[1-9]45|-1[0-3]00|-1[0-3]15|-1[0-3]30|-1[0-3]45)$",
//                "+0131");
//        System.out.println(b);

        boolean b = RegexPatternUtil.simpleMatch("^354(\\d{7}|\\d{9})$", "354000000123");
        System.out.println(b);
//        BigDecimal i = new BigDecimal("1.9");
//
//        System.out.println(i.longValue());
//
//        System.out.println(i.compareTo(new BigDecimal(0)) >= 0);
//        System.out.println(new BigDecimal(i.longValue()).compareTo(i) == 0);



    }


}
