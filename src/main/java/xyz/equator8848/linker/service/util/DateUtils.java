package xyz.equator8848.linker.service.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String parseDateTimeFromSeconds(long second) {
        Date date = new Date(second * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日HH时");
        return formatter.format(date);
    }
}
