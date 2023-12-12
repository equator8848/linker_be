package xyz.equator8848.linker.service.util;

import java.math.BigInteger;

public class TimeTransformUtil {
    public static final long ONE_MINUTE = 60L;
    public static final BigInteger ONE_MINUTE_BI = BigInteger.valueOf(ONE_MINUTE);

    public static final long HALF_HOUR = 1800L;
    public static final BigInteger HALF_HOUR_BI = BigInteger.valueOf(HALF_HOUR);

    public static final long ONE_HOUR = 3600L;
    public static final BigInteger ONE_HOUR_BI = BigInteger.valueOf(ONE_HOUR);

    public static final long ONE_DAY = 86400L;
    public static final BigInteger ONE_DAY_BI = BigInteger.valueOf(ONE_DAY);

    public static String secondToDisplayTime(BigInteger size) {
        String displaySize;
        if (size.divide(ONE_HOUR_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = size.divide(ONE_HOUR_BI) + " 小时";
        } else if (size.divide(ONE_MINUTE_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = size.divide(ONE_MINUTE_BI) + " 分钟";
        } else {
            displaySize = size + " 秒";
        }
        return displaySize;
    }

    public static String secondToDisplayTime(long size) {
        return secondToDisplayTime(BigInteger.valueOf(size));
    }

    public static BigInteger secondToDisplayTime(BigInteger size, BigInteger threshold) {
        BigInteger displaySize;
        if (ONE_HOUR_BI.equals(threshold)) {
            displaySize = size.divide(ONE_HOUR_BI);
        } else if (ONE_MINUTE_BI.equals(threshold)) {
            displaySize = size.divide(ONE_MINUTE_BI);
        } else {
            displaySize = size;
        }
        return displaySize;
    }

    public static BigInteger secondToDisplayTime(long size, BigInteger threshold) {
        return secondToDisplayTime(BigInteger.valueOf(size), threshold);
    }

    public static String secondToDisplayFloorTime(BigInteger size) {
        String displaySize;
        if (size.divide(ONE_DAY_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = "天级别";
        } else if (size.divide(ONE_HOUR_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = "小时级别";
        } else if (size.divide(HALF_HOUR_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = "30分钟~1小时";
        } else if (size.divide(ONE_MINUTE_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = "30分钟以内";
        } else {
            displaySize = "秒级别";
        }
        return displaySize;
    }

    public static String secondToDisplayFloorTime(long size) {
        return secondToDisplayFloorTime(BigInteger.valueOf(size));
    }
}
