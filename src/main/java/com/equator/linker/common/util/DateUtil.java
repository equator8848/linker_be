package com.equator.linker.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * @Author: Equator
 * @Date: 2022/7/2 18:22
 **/

public class DateUtil {
    /***
     * EEE MMM dd HH:mm:ss zzz yyyy ：Sat Jul 02 18:33:56 GMT+08:00 2022
     * @return
     */
    private static String getGMTTime(ZoneId zone) {
        // 目标： Sat, 02 Jul 2022 10:19:36 GMT
        return LocalDateTime.now(zone).format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.UK));
    }

    public static String getUTCTime() {
        return getGMTTime(ZoneId.of("UTC"));
    }

    public static String getGMTTime() {
        // 目标： Sat, 02 Jul 2022 10:19:36 GMT
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.UK));
    }

    public static Date parseAwsDate(String dateStr) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }

    public static String getAwsDate() {
        return LocalDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'", Locale.UK));
    }

    private static final DateTimeFormatter userFriendlyDateFormatter = DateTimeFormatter.ofPattern("yyyy年M月d日 HH:mm");

    public static String getUserFriendlyDateStr() {
        return LocalDateTime.now().format(userFriendlyDateFormatter);
    }

    public static String getUserFriendlyDateStr(LocalDateTime now) {
        return now.format(userFriendlyDateFormatter);
    }

    public static void main(String[] args) {
        System.out.println(getGMTTime());
        System.out.println(getGMTTime(ZoneId.of("UTC")));
        System.out.println(new Date().toString());
        System.out.println(new Date().toInstant().toString());
        System.out.println(parseAwsDate("20220706T003622Z"));
        System.out.println(getAwsDate());
    }
}
