package com.equator.linker.common.util;

import com.equator.core.model.exception.VerifyException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Equator
 * @Date: 2021/11/25 23:56
 **/

public class RegexUtil {

    private static final String PROTOCOL_DOMAIN_PATTERN = "(http|https|ftp)://(.*?)/";

    private static final String FTP_PATTERN = "(ftp)://(.*?)(/.*)";

    /**
     * 提取域名
     *
     * @param url
     * @return
     */
    public static String matchDomain(String url) {
        return match(url, PROTOCOL_DOMAIN_PATTERN, 2);
    }


    /**
     * 提取域名
     *
     * @param url
     * @return
     */
    public static Pair<String, String> matchProtocolDomain(String url) {
        Pattern p = Pattern.compile(PROTOCOL_DOMAIN_PATTERN);
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            return Pair.of(matcher.group(1), matcher.group(2));
        }
        throw new VerifyException("不支持的协议或域名");
    }

    /**
     * 匹配奶牛快传下载链接
     *
     * @param text
     * @return
     */
    public static String matchCowTransferDownloadUrl(String text) {
        String target = match(text, "Destination:(.*?)Local:", 1);
        if (!StringUtils.isEmpty(target)) {
            return target.trim();
        }
        return null;
    }

    public static String match(String text, String pattern, int group) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(text);
        if (matcher.find()) {
            return matcher.group(group);
        }
        return null;
    }


}
