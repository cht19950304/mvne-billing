package com.cmit.mvne.billing.preparation.service.impl;

import com.cmit.mvne.billing.preparation.service.CdrDupCheckService;
import com.cmit.mvne.billing.preparation.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 查重服务类
 *
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2019/7/5
 */
@Service
@Slf4j
public class CdrDupCheckServiceImpl implements CdrDupCheckService {
    @Value(value = "${process.dupOn}")
    private Integer dupOn;
    @Value(value = "${process.dupExpire}")
    private Integer dupExpire;

    @Autowired
    @Qualifier("redisTemplateTrans")
    private RedisTemplate<String, Object> redisTemplateTrans;
    @Autowired
    @Qualifier("redisTemplateN")
    private RedisTemplate<String, Object> redisTemplateN;

    private String gprsDupKey = "gprsDupKey";
    private String gsmDupKey = "gsmDupKey";
    private String smsDupKey = "smsDupKey";
    private String mmsDupKey = "mmsDupKey";


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public String checkDup(String[] cdr, String originFilename, Date receiveDate, Set<String> cdrsThisTime, long lineNum) {

        String result = null;

        if(cdr[0].equals("GP") || cdr[0].equals("RGP")) {
            result = checkGprsDup(cdr, originFilename, receiveDate, cdrsThisTime, lineNum);
        }
        if(cdr[0].equals("SMS") || cdr[0].equals("RMT") || cdr[0].equals("RSMO")) {
            result = checkSmsDup(cdr, originFilename, receiveDate, cdrsThisTime, lineNum);
        }

        //checkGsmDup(cdrFile);
        //checkMmsDup(cdrFile);

        return result;
    }

    @Override
    public void cleanDupValue(String filename) {
        redisTemplateTrans.delete("filename:" + filename);
        //redisTemplate.opsForSet().difference(gprsDupKey, filename + ".redo");
        /*redisTemplate.opsForSet().difference(gsmDupKey, filename + ".gsm.redo");
        redisTemplate.opsForSet().difference(smsDupKey, filename + ".sms.redo");
        redisTemplate.opsForSet().difference(mmsDupKey, filename + "mms.redo");*/

    }

    @Override
    public void createRedoLog(String originFilename) {
        String redoLog = originFilename + ".redo";
        redisTemplateTrans.opsForSet().add(redoLog, originFilename);
    }

    @Override
    public void deleteRedoLog(String originFilename) {
        String redoLog = originFilename + ".redo";
        redisTemplateTrans.delete(redoLog);
    }

    @Override
    public void deleteDupLog(String originFilename) {
        String dupLog = originFilename + ".dup";
        redisTemplateTrans.delete(dupLog);
    }

    @Override
    public Boolean existRedoLog(String originFilename) {
        String redoLog = originFilename + ".redo";
        return redisTemplateTrans.opsForSet().isMember(redoLog, originFilename);
    }


    /**
     * 流量话单查重
     * @param cdr 话单记录 数组
     * @param originFilename 原始文件名
     * @return
     */
    @Override
    public String checkGprsDup(String[] cdr, String originFilename, Date receiveDate, Set<String> cdrsThisTime, long lineNum) {

        String redoLog = originFilename + ".redo";
        String dupLog = originFilename + ".dup";

        // 可以配置是否查重
        if (dupOn == 1) {
            boolean result = doCheckGprsDup(cdr, redoLog, dupLog, receiveDate, cdrsThisTime, lineNum);
            if (result) {
                // 重单
                return "Severe255";
            }
        }

        return null;
    }

    /**
     * 查重，每条话单查重需要控制在一个事务中
     * @param cdr 待查重话单
     * @param redoLog 日志
     * @return 返回True表示为重单
     */
    public boolean doCheckGprsDup(String[] cdr, String redoLog, String dupLog, Date receiveDate, Set<String> cdrsThisTime, long lineNum) {
        Boolean result = false;
        String gprsIdentify = getGprsIdentify(cdr);
        String gprsIdentifyWithNum = getGprsIdentifyWithNum(cdr, lineNum);
        // 判断话单内是否重单
        Boolean haveDealThisTime = cdrsThisTime.contains(gprsIdentify);
        // 判断之前是否处理过
        Boolean haveDealBefore = redisTemplateN.opsForSet().isMember(redoLog, gprsIdentifyWithNum);
        Boolean isDup = redisTemplateN.opsForSet().isMember(dupLog, gprsIdentifyWithNum);
        String dupKey = gprsDupKey + ":" + DateTimeUtil.dateToString(receiveDate);
        long number = redisTemplateN.opsForSet().add(dupKey, gprsIdentify);

        // 不管怎样，都写redolog，记录是否处理过该话单
        redisTemplateTrans.opsForSet().add(redoLog, gprsIdentifyWithNum);

        if (isDup) {
            /*if (!haveDealThisTime && haveDealBefore) {
                result = false;
            } else {
                result = true;
            }*/
            result = true;
        } else {
            // 如果不是成员，说明（还没处理过该条话单），（或者话单不是重单）
            // 查重

            redisTemplateTrans.expire(dupKey, dupExpire, TimeUnit.DAYS);
            if(number > 0) {
                // 非重单
                result = false;
            } else {
                if (haveDealThisTime) {
                    // 如果这次就处理过，必然重单
                    redisTemplateTrans.opsForSet().add(dupLog, gprsIdentifyWithNum);
                    result = true;
                } else {
                    // 如果这次没处理过，但是gprsdupkey里面又有
                    if (haveDealBefore) {
                        // 如果是因为之前处理过导致gprsdupkey，则不是重单
                        result = false;
                    } else {
                        // 如果不是之前处理过导致的，则必然是处理过相同话单
                        redisTemplateTrans.opsForSet().add(dupLog, gprsIdentifyWithNum);
                        result = true;
                    }
                }
            }
        }

        // 写入本次的处理结果
        cdrsThisTime.add(gprsIdentify);
        return result;
    }

    @Override
    public Boolean dupOrNot(Boolean haveDealThisTime, Boolean haveDealBefore, Boolean isDup, long number) {
        Boolean result = false;

        if (isDup) {
            /*if (!haveDealThisTime && haveDealBefore) {
                result = false;
            } else {
                result = true;
            }*/
            result = true;
        } else {
            // 如果不是成员，说明（还没处理过该条话单），（或者话单不是重单）
            // 查重

            if(number > 0) {
                // 非重单
                result = false;
            } else {
                if (haveDealThisTime) {
                    // 如果这次就处理过，必然重单
                    result = true;
                } else {
                    // 如果这次没处理过，但是gprsdupkey里面又有
                    if (haveDealBefore) {
                        // 如果是因为之前处理过导致gprsdupkey，则不是重单
                        result = false;
                    } else {
                        // 如果不是之前处理过导致的，则必然是处理过相同话单
                        result = true;
                    }
                }
            }
        }

        return result;
    }

    /**
     * 获取流量话单查重标识
     * @param cdr
     * @return
     */
    @Override
    public String getGprsIdentifyWithNum(String[] cdr, long lineNum) {
        StringBuilder sb = new StringBuilder();

        return sb.append(cdr[0])
                .append(cdr[4]) // msisdn
                .append(cdr[5])  // imsi
                .append(cdr[6])  // EventTimestamp
                .append(cdr[24]) // CallIdentification
                .append("|")
                .append(lineNum)
                .toString();

    }

    /**
     * 获取流量话单查重标识
     * @param cdr
     * @return
     */
    @Override
    public String getGprsIdentify(String[] cdr) {
        StringBuilder sb = new StringBuilder();

//        return DigestUtils.md5Hex(sb.append(msisdn)
//                .append(imsi)
//                .append(eventTimestamp)
//                .append(callIdentification).toString());
        return sb.append(cdr[0])
                .append(cdr[4]) // msisdn
                .append(cdr[5])  // imsi
                .append(cdr[6])  // EventTimestamp
                .append(cdr[24]) // CallIdentification
                .toString();

    }


//    /**
//     * 语音话单查重
//     * @param cdrFile 话单文件对象
//     */
//    public void checkGsmDup(CdrFile cdrFile) {
//        String redoLog = cdrFile.getOriginFile().getName() + ".gsm.redo";
//        List<CdrGsm> cdrGsmList = cdrFile.cloneGsmCdrs();
//        for(CdrGsm gsm : cdrGsmList) {
//            boolean result = doCheckGsmDup(gsm, redoLog);
//            if (result) {
//                cdrFile.getCdrGsmList().remove(gsm);
////                cdrFile.getDupCdrGsmsList().add(gsm);
//            }
//        }
//    }
//
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    protected boolean doCheckGsmDup(CdrGsm gsm, String redoLog) {
//        long number = redisTemplate.opsForSet().add(gsmDupKey, gsm.getDupKey()).longValue();
//        if(number > 0) {
//            // 非重单 记录添加查重集合中的key ，用于异常中断时回退重处理
//            redisTemplate.opsForSet().add(redoLog, gsm.getDupKey());
//            return false;
//        }
//        return true;
//    }


    /**
     * 短信话单查重
     * @param cdr 话单 数组
     * @param originFilename 原始文件名
     * @return errorCode
     */
    @Override
    public String checkSmsDup(String[] cdr, String originFilename, Date receiveDate, Set<String> cdrsThisTime, long lineNum) {

        String redoLog = originFilename + ".redo";
        String dupLog = originFilename + ".dup";

        // 可以配置是否查重
        if (dupOn == 1) {
            boolean result = doCheckSmsDup(cdr, redoLog, dupLog, receiveDate, cdrsThisTime, lineNum);
            if (result) {
                // 重单
                if (cdr[0].equals("RSMO") || cdr[0].equals("SMS")) {
                    return "Severe250";
                } else {
                    return "Severe251";
                }
            }
        }
        return null;

    }

    public boolean doCheckSmsDup(String[] cdr, String redoLog, String dupLog, Date receiveDate, Set<String> cdrsThisTime, long lineNum) {
        Boolean result = false;
        String smsIdentify = getSmsIdentify(cdr);
        String smsIdentifyWithNum = getSmsIdentifyWithNum(cdr, lineNum);
        // 判断话单内是否重单
        Boolean haveDealThisTime = cdrsThisTime.contains(smsIdentify);
        // 判断之前是否处理过
        Boolean haveDealBefore = redisTemplateN.opsForSet().isMember(redoLog, smsIdentifyWithNum);
        Boolean isDup = redisTemplateN.opsForSet().isMember(dupLog, smsIdentifyWithNum);
        String dupKey = smsDupKey + ":" + DateTimeUtil.dateToString(receiveDate);
        long number = redisTemplateN.opsForSet().add(dupKey, smsIdentify);

        // 不管怎样，都写redolog，记录是否处理过该话单
        redisTemplateTrans.opsForSet().add(redoLog, smsIdentifyWithNum);

        if (isDup) {
            /*if (!haveDealThisTime && haveDealBefore) {
                result = false;
            } else {
                result = true;
            }*/
            result = true;
        } else {
            // 如果不是成员，说明（还没处理过该条话单），（或者话单不是重单）
            // 查重

            redisTemplateTrans.expire(dupKey, dupExpire, TimeUnit.DAYS);
            if(number > 0) {
                // 非重单
                result = false;
            } else {
                if (haveDealThisTime) {
                    // 如果这次就处理过，必然重单
                    redisTemplateTrans.opsForSet().add(dupLog, smsIdentifyWithNum);
                    result = true;
                } else {
                    // 如果这次没处理过，但是smsdupkey里面又有
                    if (haveDealBefore) {
                        // 如果是因为之前处理过导致smsdupkey，则不是重单
                        result = false;
                    } else {
                        // 如果不是之前处理过导致的，则必然是处理过相同话单
                        redisTemplateTrans.opsForSet().add(dupLog, smsIdentifyWithNum);
                        result = true;
                    }
                }
            }
        }

        // 写入本次的处理结果
        cdrsThisTime.add(smsIdentify);
        return result;
    }

    /**
     * 获取短信话单查重标识
     * @param cdr
     * @return
     */
    @Override
    public String getSmsIdentify(String[] cdr) {
        StringBuilder sb = new StringBuilder();
        // 主叫话单
        if(cdr[0].equals("RSMO") || cdr[0].equals("SMS")) {
            sb.append(cdr[4]) // MSISND
                    .append(cdr[5]) // IMSI
                    .append(cdr[2]) // Number B
                    .append(cdr[6]) // EventTimestamp
                    .append(cdr[24]); // CallIdentification
        } else { // 被叫话单
            sb.append(cdr[4]) // MSISND
                    .append(cdr[5]) // IMSI
                    .append(cdr[6]) // EventTimestamp
                    .append(cdr[24]); // CallIdentification
        }
//        return DigestUtils.md5Hex(sb.toString());
        return sb.toString();
    }

    @Override
    public String getSmsIdentifyWithNum(String[] cdr, long lineNum) {
        StringBuilder sb = new StringBuilder();
        // 主叫话单
        if(cdr[0].equals("RSMO") || cdr[0].equals("SMS")) {
            sb.append(cdr[4]) // MSISND
                    .append(cdr[5]) // IMSI
                    .append(cdr[2]) // Number B
                    .append(cdr[6]) // EventTimestamp
                    .append(cdr[24]); // CallIdentification
        } else { // 被叫话单
            sb.append(cdr[4]) // MSISND
                    .append(cdr[5]) // IMSI
                    .append(cdr[6]) // EventTimestamp
                    .append(cdr[24]); // CallIdentification
        }

        return sb.append("|").append(lineNum).toString();
    }


//    /**
//     * 彩信话单查重
//     * @param cdrFile 话单文件对象
//     */
//    public void checkMmsDup(CdrFile cdrFile) {
//
//        String redoLog = cdrFile.getOriginFile().getName() + ".mms.redo";
//        List<CdrMms> cdrMmsList = cdrFile.cloneMmsCdrs();
//        for (CdrMms mms : cdrMmsList) {
//            boolean result = doCheckMmsDup(mms, redoLog);
//            if (result) {
//                // 重单
//                cdrFile.getCdrMmsList().remove(mms);
////                cdrFile.getDupCdrMmsList().add(mms);
//            }
//        }
//    }
//
//    protected boolean doCheckMmsDup(CdrMms mms, String redoLog) {
//        long number = redisTemplate.opsForSet().add(mmsDupKey, mms.getDupKey()).longValue();
//        if (number > 0) {
//            // 非重单 记录添加查重集合中的key ，用于异常中断时回退重处理
//            redisTemplate.opsForSet().add(redoLog, mms.getDupKey());
//            return false;
//        }
//        return true;
//    }
}
