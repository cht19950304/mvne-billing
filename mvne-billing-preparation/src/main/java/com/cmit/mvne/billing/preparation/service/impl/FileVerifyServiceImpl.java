package com.cmit.mvne.billing.preparation.service.impl;

import com.cmit.mvne.billing.preparation.service.FileVerifyService;
import com.cmit.mvne.billing.preparation.util.DateTimeUtil;
import com.cmit.mvne.billing.preparation.util.RegexPatternUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/4/8
 */
@Service
@Slf4j
public class FileVerifyServiceImpl implements FileVerifyService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String verifyFile(File workFile) {
        // 文件名校验
//        String fileName = srcFile.getName().split(".")[0];
        String fileName = workFile.getName().substring(0, workFile.getName().lastIndexOf('.'));
        boolean isMatch = RegexPatternUtil.simpleMatch("^(S4PGWB|S4PGWM|S4SVRO)\\d{6}$", fileName);
        if(!isMatch) {
            log.error("file {} is not matche rules(S4PGWBnnnnnn|S4PGWMnnnnnn|S4SVROnnnnnn", fileName);
            return "Fatal200";
        }

        // 判断文件超期
        LocalDateTime fileDateTime = DateTimeUtil.getDateTimeofTimestamp(workFile.lastModified());
        // 漫游话单
        if(fileName.startsWith("S4SVRO")) {
            if(LocalDateTime.now().minusDays(30).isAfter(fileDateTime)) {
                log.error("file {}, file time {} is 30 days later than now", fileName, fileDateTime);
                return "Fatal300";
            } else if(LocalDateTime.now().plusDays(1).isBefore(fileDateTime)){
                log.error("file {}, file time {} is 1 day early than now", fileName, fileDateTime);
                return "Fatal400";
            }
        // 非漫游话单
        } else {
            if(LocalDateTime.now().minusDays(15).isAfter(fileDateTime)) {
                log.error("file {}, file time {} is 15 days later than now", fileName, fileDateTime);
                return "Fatal300";
            } else if(LocalDateTime.now().plusDays(1).isBefore(fileDateTime)){
                log.error("file {}, file time {} is 1 day early than now", fileName, fileDateTime);
                return "Fatal400";
            }
        }


        // 判断文件级重复
        if (filenameDupCheck(fileName)) return "Fatal100";

//        // 判断文件跳号
//        // todo 存在并发问题，待修复
        // 未避免跳号后系统异常导致序号混乱，文件跳号放到入库时处理
        serialNumberCheck(fileName);

        return null;
    }

    /**
     * 文件跳号处理
     * @param fileName 文件名称
     */
    @Transactional
    protected void serialNumberCheck(String fileName) {
        final String filestream = getFilestream(fileName);
        int serialNumber = Integer.parseInt(getSerialNumber(fileName));
        String preNumber = redisTemplate.opsForValue().get(filestream);
//        int preNumber = Integer.parseInt(redisTemplate.opsForValue().get(filestream));
        if(StringUtils.isEmpty(preNumber)) {
            // 首次
            redisTemplate.opsForValue().set(filestream, String.valueOf(serialNumber));
        } else {
//            int increNumber = redisTemplate.opsForValue().increment(filestream, 1).intValue();
            // 特殊情况
            int preNumberInt = Integer.parseInt(preNumber);
            if (preNumberInt == 999999) {
                // 跳号
                if(serialNumber != 0) {
                    log.warn("Fatal500: file {} is not continuous, Previous file is {}", fileName, filestream + serialNumberFormat(preNumberInt));
                }
                redisTemplate.opsForValue().set(filestream, String.valueOf(serialNumber));
            } else {
                // 跳号
                if (serialNumber >  preNumberInt + 1) {
                    log.warn("Fatal500: file {} is not continuous, Previous file is {}", fileName, filestream + serialNumberFormat(preNumberInt));
                    // 更新文件序号
                    redisTemplate.opsForValue().set(filestream, String.valueOf(serialNumber));
                    // 不返回错误编码，仍然继续处理
                } else if(serialNumber == preNumberInt + 1) {
                    redisTemplate.opsForValue().increment(filestream, 1);
                } else {
                    log.warn("Fatal500: file {} is not continuous, Previous file is {}", fileName, filestream + serialNumberFormat(preNumberInt));
                }
            }

        }
    }

    public String serialNumberFormat(int number) {
        String str = String.format("%06d", number);
        return str;
    }

    @Transactional
    protected boolean filenameDupCheck(String fileName) {
        final String key = "filename:" + fileName;
        final String result = redisTemplate.opsForValue().get(key);
        if(StringUtils.isEmpty(result)) {
            //redisTemplate.opsForValue().set(key, fileName, 30, TimeUnit.DAYS);
        }else {
            return true;
        }
        return false;
    }

    @Override
    public void deleteFileDupKey(String fileName) {
        final String key = "filename:" + fileName;
        redisTemplate.delete(key);
    }

    private String getFilestream(String fileName) {
        return fileName.substring(0, fileName.length()-6);
    }

    private String getSerialNumber(String fileName) {
        return fileName.substring(fileName.length()-6);
    }

    private static String parseToSerialString(String number) {
        char[] result = new char[6];
        int j = result.length-1;

        char[] chars = number.toCharArray();

        for (int i=chars.length-1; i>=0; i--) {
            result[j--] = chars[i];
        }
        while (j>=0) {
            result[j] = '0';
            j--;
        }

        return new String(result);
    }

    public static void main(String[] args) {
//        System.out.println(RegexPatternUtil.simpleMatch("^(S4PGWB|S4PGWM|S4SVRO)\\d{6}$", "S4SVRO000031"));
//
//        String filename = "S4SVRT000031.writing.dealing";
//        System.out.println(filename.substring(0, filename.lastIndexOf('.')));

//        String s = parseToSerialString("121123");
//        System.out.println(s);

//        System.out.println(serialNumberFormat(911));
    }
}
