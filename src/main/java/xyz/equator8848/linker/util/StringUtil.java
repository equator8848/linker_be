package xyz.equator8848.linker.util;

import org.springframework.util.StringUtils;

/**
 * @Author: Equator
 * @Date: 2022/7/2 18:57
 **/

public class StringUtil {
    public static String trimSuffix(String source, String suffix) {
        if (source.endsWith(suffix)) {
            return source.substring(0, source.lastIndexOf(suffix));
        }
        return source;
    }

    public static String cutoff(String source, int length) {
        if (StringUtils.isEmpty(source) || source.length() < length) {
            return source;
        }
        return source.substring(0, length);
    }
}
