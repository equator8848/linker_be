package xyz.equator8848.linker.service.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

public class DateTimeUtils {
    public static String getTimeFromISO8601(String timeStr) {
        DateTime parseDate = DateUtil.parse(timeStr);
        return DateUtil.formatDateTime(parseDate);
    }
}
