package xyz.equator8848.linker.service.util;

import java.math.BigInteger;

public class SizeTransformUtil {
    public static final long ONE_KB = 1024L;
    public static final BigInteger ONE_KB_BI = BigInteger.valueOf(ONE_KB);
    public static final long ONE_MB = 1048576L;
    public static final BigInteger ONE_MB_BI = BigInteger.valueOf(ONE_MB);
    public static final long ONE_GB = 1073741824L;
    public static final BigInteger ONE_GB_BI = BigInteger.valueOf(ONE_GB);
    public static final long ONE_TB = 1099511627776L;
    public static final BigInteger ONE_TB_BI = BigInteger.valueOf(ONE_TB);
    public static final long ONE_PB = 1125899906842624L;
    public static final BigInteger ONE_PB_BI = BigInteger.valueOf(ONE_PB);
    public static final long ONE_EB = 1152921504606846976L;
    public static final BigInteger ONE_EB_BI = BigInteger.valueOf(ONE_EB);

    public static String byteCountToDisplaySize(BigInteger size) {
        String displaySize;
        if (size.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = size.divide(ONE_EB_BI) + " EB";
        } else if (size.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = size.divide(ONE_PB_BI) + " PB";
        } else if (size.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = size.divide(ONE_TB_BI) + " TB";
        } else if (size.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = size.divide(ONE_GB_BI) + " GB";
        } else if (size.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = size.divide(ONE_MB_BI) + " MB";
        } else if (size.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = size.divide(ONE_KB_BI) + " KB";
        } else {
            displaySize = size + " bytes";
        }
        return displaySize;
    }

    public static String byteCountToDisplaySize(long size) {
        return byteCountToDisplaySize(BigInteger.valueOf(size));
    }

    public static BigInteger byteToDisplaySize(BigInteger size, BigInteger threshold) {
        BigInteger displaySize;
        if (ONE_EB_BI.equals(threshold)) {
            displaySize = size.divide(ONE_EB_BI);
        } else if (ONE_PB_BI.equals(threshold)) {
            displaySize = size.divide(ONE_PB_BI);
        } else if (ONE_TB_BI.equals(threshold)) {
            displaySize = size.divide(ONE_TB_BI);
        } else if (ONE_GB_BI.equals(threshold)) {
            displaySize = size.divide(ONE_GB_BI);
        } else if (ONE_MB_BI.equals(threshold)) {
            displaySize = size.divide(ONE_MB_BI);
        } else if (ONE_KB_BI.equals(threshold)) {
            displaySize = size.divide(ONE_KB_BI);
        } else {
            displaySize = size;
        }
        return displaySize;
    }

    public static BigInteger byteToDisplaySize(long size, BigInteger threshold) {
        return byteToDisplaySize(BigInteger.valueOf(size), threshold);
    }
}
