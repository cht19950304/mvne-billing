package com.cmit.mvne.billing.rating.gprs.controller;

import com.cmit.mvne.billing.user.analysis.common.MvneException;
import com.cmit.mvne.billing.user.analysis.service.CdrGprsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Stream;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/5/14 11:11
 */

@Slf4j
@RestController
@RequestMapping("/dailyManagement")
public class DailyManagementController {

    @Autowired
    CdrGprsService cdrGprsService;

    /**
     * 详单建表接口，日表。月表的话自己建啦
     * @param startDate
     * @param endDate
     */
    @RequestMapping(value = "/createCdrTable", method = RequestMethod.POST)
    public String createTableLikeModel(@RequestParam(value = "startDate") String startDate,
                                       @RequestParam(value = "endDate") String endDate) {
        DateTimeFormatter dateDtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate start = LocalDate.parse(startDate, dateDtf);
        LocalDate end = LocalDate.parse(endDate, dateDtf);
        long distance = ChronoUnit.DAYS.between(start, end);
        if (distance < 0) {
            return "endDate should be later than startDate";
        }

        // 约1秒一个月，很快
        // 日期遍历
        Stream.iterate(start, d -> {
            return d.plusDays(1);
        }).limit(distance + 1).forEach(f -> {
            String date = f.format(dateDtf);
            cdrGprsService.createTableByDate(date, "yyyyMMdd");
        });
        return "success";
    }
}
