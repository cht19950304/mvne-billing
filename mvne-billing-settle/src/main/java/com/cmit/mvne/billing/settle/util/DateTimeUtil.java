package com.cmit.mvne.billing.settle.util;

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


    public static Date strToDate(String dateTimeStr, String offSet) {
        Instant instant = LocalDateTime.parse(dateTimeStr, basicFormater).toInstant(ZoneOffset.of(offSet));
        return Date.from(instant);
    }
    public static Date strToDate(String dateTimeStr) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, basicFormater);
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static LocalDateTime parseByBasicFormater(String dateTimeStr) throws DateTimeParseException {
        return LocalDateTime.parse(dateTimeStr, basicFormater);
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
        Instant instant = date.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        return zonedDateTime.toLocalDateTime();
    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getDefault());
        String str = "20200215104111";
        System.out.println(strToDate(str, "+00"));
    }
}
