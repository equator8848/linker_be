package com.equator.linker.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @Author: Equator
 * @Date: 2022/6/12 23:04
 **/

public class FormatUtil {
    private static final long KB = 1024;

    private static final long MB = KB * KB;

    private static final long GB = KB * MB;

    /**
     * 文件大小格式化
     *
     * @param fileSize
     * @return
     */
    public static String fileSizePretty(long fileSize) {
        DecimalFormat df = new DecimalFormat("0.00000");
        df.setRoundingMode(RoundingMode.HALF_UP);
        if (fileSize > GB) {
            return String.format("%sGB", df.format(fileSize * 1.0 / GB));
        } else if (fileSize > MB) {
            return String.format("%sMB", df.format(fileSize * 1.0 / MB));
        } else {
            return String.format("%sKB", df.format(fileSize * 1.0 / KB));
        }
    }


    private static final long SECOND = 1;

    private static final long MINUTES = 60 * SECOND;

    private static final long HOURS = 60 * MINUTES;

    /**
     * 时间格式化
     *
     * @param timeSeconds
     * @return
     */
    public static String timePretty(long timeSeconds) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        if (timeSeconds > HOURS) {
            return String.format("%s小时", df.format(timeSeconds * 1.0 / HOURS));
        } else if (timeSeconds > MINUTES) {
            return String.format("%s分钟", df.format(timeSeconds * 1.0 / MINUTES));
        } else {
            return String.format("%s秒", df.format(timeSeconds * 1.0 / SECOND));
        }
    }

    public static String msTimePretty(Long timeMs) {
        if (timeMs == null) {
            return "-";
        }
        return timePretty(timeMs / 1000);
    }


}
