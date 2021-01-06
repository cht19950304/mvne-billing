package com.cmit.mvne.billing.preparation.util;

import org.springframework.format.datetime.DateFormatter;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/2/14
 */
public class DateTimeUtil {

    private static DateTimeFormatter basicFormater = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static String dayFormater = "yyyyMMdd";


    public static Date strToDate(String dateTimeStr, String offSet) {
        Instant instant = LocalDateTime.parse(dateTimeStr, basicFormater).toInstant(ZoneOffset.of(offSet));
        return Date.from(instant);
    }

    public static LocalDateTime parseByBasicFormater(String dateTimeStr) throws DateTimeParseException {
        return LocalDateTime.parse(dateTimeStr, basicFormater);
    }

    public static String dateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dayFormater);
        return simpleDateFormat.format(date);
    }

    public static LocalDateTime getDateTimeofTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static Date getDateofTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return Date.from(instant);
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getDefault());
        String str = "20200215104111";
        System.out.println(strToDate(str, "+00"));
    }
}
