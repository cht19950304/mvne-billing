package com.cmit.mvne.billing.rating.gprs.util;

import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/2/14 15:48
 */
public class DateUtils {
    public static Date getLocalEventTimestamp(Date eventTimestamp, String UTCoffset) {
        /*if (UTCoffset.startsWith("+")) {
            UTCoffset.replace("+", "-");
        } else {
            UTCoffset.replace("-", "+");
        }*/

        // 必须通过间隔纠正时间
        // 得到没有时区的漫游地的localdatetime
        Instant instant = eventTimestamp.toInstant();
        ZoneId zoneId = ZoneId.of("+00");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);

        // 转成带有时区的zone的DateTime
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of(UTCoffset));

        // 转换成冰岛时区的时间
        ZonedDateTime iceLand = zonedDateTime.withZoneSameInstant(zoneId);
        Date date = Date.from(iceLand.toInstant());

        return date;

    }

    public static String Date2Str(Date date, String formatter) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);

        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        String dateStr = dateTimeFormatter.format(localDateTime);

        return dateStr;
    }

}
