package com.cmit.mvne.billing.preparation.service.impl;

import com.cmit.mvne.billing.preparation.entity.*;
import com.cmit.mvne.billing.preparation.service.CdrDupCheckService;
import com.cmit.mvne.billing.preparation.service.CdrParsingService;
import com.cmit.mvne.billing.preparation.service.CdrVerifyService;
import com.cmit.mvne.billing.preparation.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 话单解析，包括  解码、校验、查重
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/2/14
 */
@Service
@Slf4j
public class CdrParsingServiceImpl implements CdrParsingService {

    @Autowired
    private CdrVerifyService verifyService;
    @Autowired
    private CdrDupCheckService dupCheckService;

    public final static String cdrSplit = "\\|";

    @Override
    public void parsing(CdrFile cdrFile) throws IOException {

        File workFile = cdrFile.getWorkFile();

        log.info("Starting Parsing file {}", workFile.getName());
        BufferedReader bufferedReader = null;
        try {
//            bufferedReader = new BufferedReader(new FileReader(workFile)); // 采用的是默认字符编码
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(workFile), StandardCharsets.UTF_8));

            String line ;
            // 话单接收时间
            Date fileReceiveDate = DateTimeUtil.getDateofTimestamp(cdrFile.getWorkFile().lastModified());
            String fileName = cdrFile.getOriginFile().getName();
            // 记录本次处理的话单，用于判断同一个文件的重单
            Set<String> cdrsThisTime = new HashSet<>();
            long lineNum = 0;
            while((line = bufferedReader.readLine()) != null) {
                lineNum++;
                String[] cdr = parseToArray(line);

                log.debug("Verify cdr :{}", line);
                // 话单级校验
                String verifyResult = verifyService.verify(cdr, fileReceiveDate);
                // 未通过校验
                if (!StringUtils.isEmpty(verifyResult)) {
                    cdrFile.getErrorCdrList().add(verifyResult + "|" + lineNum + "|" + line);
                    continue;
                }
                log.debug("Verify result :{}, lineNumber: {}", verifyResult, lineNum);


                // 话单查重
                log.debug("Check duplicate : {}", line);
                String dupCheckResult = dupCheckService.checkDup(cdr, cdrFile.getOriginFile().getName(), DateTimeUtil.strToDate(cdr[6], cdr[13]), cdrsThisTime, lineNum);
                // 未通过查重
                if(!StringUtils.isEmpty(dupCheckResult)) {
                    cdrFile.getErrorCdrList().add(dupCheckResult + "|" + lineNum + "|" + line);
                    continue;
                }
                log.debug("Check duplicate result : {}", dupCheckResult);

                cdrFile.getSuccessCdrList().add(line);
                // transform and sort
                transformCdr(cdr, cdrFile, lineNum);

            }

            if(lineNum == 0) log.warn("File: {} is a empty file!", workFile.getAbsolutePath());

            log.info("Decode file {}, success! the number of cdr is {}", workFile.getName(), lineNum);

        } catch (FileNotFoundException e) {
            log.error("File not Found !", e);
            throw e;
        } catch (IOException e) {
            log.error("IO Exception.", e);
            throw e;
        }finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                log.error("close bufferedReader error!", e);
            }
        }

    }

    @Override
    public String[] parseToArray(String line) {
        return line.split(cdrSplit, -1);
    }

    @Override
    public Date getLocalTime(String[] cdr) {
        if("GP".equals(cdr[0]) || "RGP".equals(cdr[0])) {
            return DateTimeUtil.strToDate(cdr[6], cdr[13]);
        } else if("RSMO".equals(cdr[0]) || "RMT".equals(cdr[0]) || "SMS".equals(cdr[0])) {
            return DateTimeUtil.strToDate(cdr[6], cdr[13]);
        } else {
            return DateTimeUtil.strToDate(cdr[6], cdr[13]);
        }
    }

    /**
     * 将话单转换为话单对象,并保存再话单文件对象中
     * @param cdr 话单
     * @param cdrFile 话单文件对象
     */
    private void transformCdr(String[] cdr, CdrFile cdrFile, long lineNum) {

        String originFilename = cdrFile.getOriginFile().getName();
        Date receiveTime = DateTimeUtil.getDateofTimestamp(cdrFile.getWorkFile().lastModified());

        List<CdrGprsRating> cdrGprsRatingList = cdrFile.getCdrGprsRatingList();
        List<CdrGprsSettle> cdrGprsSettleList = cdrFile.getCdrGprsSettleList();
        List<CdrSmsRating> cdrSmsRatingList = cdrFile.getCdrSmsRatingList();
        List<CdrSmsSettle> cdrSmsSettleList = cdrFile.getCdrSmsSettleList();


        doTransformCdr(cdr, lineNum, originFilename, receiveTime, cdrGprsRatingList, cdrGprsSettleList, cdrSmsRatingList, cdrSmsSettleList);

    }

    @Override
    public void doTransformCdr(String[] cdr, long lineNum, String originFilename, Date receiveTime, List<CdrGprsRating> cdrGprsRatingList, List<CdrGprsSettle> cdrGprsSettleList, List<CdrSmsRating> cdrSmsRatingList, List<CdrSmsSettle> cdrSmsSettleList) {
        if("GP".equals(cdr[0]) || "RGP".equals(cdr[0])) {
            CdrGprsRating cdrGprsRating = new CdrGprsRating();
            cdrGprsRating.setRecordType(cdr[0]);
            cdrGprsRating.setNumberA(cdr[1]);
            cdrGprsRating.setNumberB(cdr[2]);
            cdrGprsRating.setNumberDialed(cdr[3]);
            cdrGprsRating.setMsisdn(cdr[4]);
            cdrGprsRating.setImsi(cdr[5]);
            cdrGprsRating.setEventTimeStamp(cdr[6]);
            cdrGprsRating.setLocalEventTimeStamp(DateTimeUtil.strToDate(cdr[6], cdr[13]));
            cdrGprsRating.setEventDuration(Integer.parseInt(cdr[7]));
            cdrGprsRating.setDownloadVol(new BigDecimal(cdr[8]));
            cdrGprsRating.setUploadVol(new BigDecimal(cdr[9]));
            cdrGprsRating.setOperatorCode(cdr[10]);
            cdrGprsRating.setPreratedAmount(cdr[11]);
            cdrGprsRating.setApn(cdr[12]);
            cdrGprsRating.setNulli(cdr[13]);
            cdrGprsRating.setBroadWorks(cdr[14]);
            cdrGprsRating.setTeleServiceCode(cdr[15]);
            cdrGprsRating.setBearerServiceCode(cdr[16]);
            cdrGprsRating.setOverseasCode(cdr[17]);
            cdrGprsRating.setVideoIndicator(cdr[18]);
            cdrGprsRating.setSource(cdr[19]);
            cdrGprsRating.setServiceId(cdr[20]);
            cdrGprsRating.setQuantity(cdr[21]);
            cdrGprsRating.setCustNumber(cdr[22]);
            cdrGprsRating.setDescription(cdr[23]);
            cdrGprsRating.setCallIdentification(cdr[24]);

            cdrGprsRating.setOriginalFile(originFilename);
            cdrGprsRating.setLineNumber(lineNum);
            cdrGprsRating.setTailNumber(cdr[4].substring(cdr[4].length()-1));
            cdrGprsRating.setReceiveTime(receiveTime);


//            cdrGprsRating.setLineNum(lineNum);

            // 构建结算话单
            CdrGprsSettle cdrGprsSettle = new CdrGprsSettle();
            BeanUtils.copyProperties(cdrGprsRating, cdrGprsSettle);

            cdrGprsRatingList.add(cdrGprsRating);
            cdrGprsSettleList.add(cdrGprsSettle);


        } else if("RSMO".equals(cdr[0]) || "RMT".equals(cdr[0]) || "SMS".equals(cdr[0])) {
            CdrSmsRating cdrSmsRating = new CdrSmsRating();
            cdrSmsRating.setRecordType(cdr[0]);
            cdrSmsRating.setNumberA(cdr[1]);
            cdrSmsRating.setNumberB(cdr[2]);
            cdrSmsRating.setNumberDialed(cdr[3]);
            cdrSmsRating.setMsisdn(cdr[4]);
            cdrSmsRating.setImsi(cdr[5]);
            cdrSmsRating.setEventTimeStamp(cdr[6]);
            cdrSmsRating.setLocalEventTimeStamp(DateTimeUtil.strToDate(cdr[6], cdr[13]));
            cdrSmsRating.setEventDuration(Integer.parseInt(cdr[7]));
            cdrSmsRating.setDownloadVol(new BigDecimal(cdr[8]));
            cdrSmsRating.setUploadVol(new BigDecimal(cdr[9]));
            cdrSmsRating.setOperatorCode(cdr[10]);
            cdrSmsRating.setPreratedAmount(cdr[11]);
            cdrSmsRating.setApn(cdr[12]);
            cdrSmsRating.setNulli(cdr[13]);
            cdrSmsRating.setBroadWorks(cdr[14]);
            cdrSmsRating.setTeleServiceCode(cdr[15]);
            cdrSmsRating.setBearerServiceCode(cdr[16]);
            cdrSmsRating.setOverseasCode(cdr[17]);
            cdrSmsRating.setVideoIndicator(cdr[18]);
            cdrSmsRating.setSource(cdr[19]);
            cdrSmsRating.setServiceId(cdr[20]);
            cdrSmsRating.setQuantity(cdr[21]);
            cdrSmsRating.setCustNumber(cdr[22]);
            cdrSmsRating.setDescription(cdr[23]);
            cdrSmsRating.setCallIdentification(cdr[24]);

            cdrSmsRating.setOriginalFile(originFilename);
            cdrSmsRating.setLineNumber(lineNum);
            cdrSmsRating.setTailNumber(cdr[4].substring(cdr[4].length()-1));
            cdrSmsRating.setReceiveTime(receiveTime);

//            cdrSmsRating.setLineNum(lineNum);

            CdrSmsSettle cdrSmsSettle = new CdrSmsSettle();
            BeanUtils.copyProperties(cdrSmsRating, cdrSmsSettle);

            cdrSmsRatingList.add(cdrSmsRating);
            cdrSmsSettleList.add(cdrSmsSettle);

        } else {
            // todo 彩信话单和语音话单格式未定
            log.error("Donnot support GSM and MMS");
        }
    }


    public static void main(String[] args) {

        // ^[+|-][0|1][0-9]00$
//        System.out.println(RegexPatternUtil.simpleMatch("^[+|-][0|1][0-9]00$", "+1900"));

//        System.out.println(RegexPatternUtil.simpleMatch("^(\\+0[0-9]\\d{2}|\\+1[0-4]\\d{2}|-0[1-9]\\d{2}|-1[0-3]\\d{2})$", "-130"));
//        System.out.println(RegexPatternUtil.simpleMatch("^\\d*$", "4324"));

        System.out.println(System.getProperty("java.io.tmpdir"));

    }


}
