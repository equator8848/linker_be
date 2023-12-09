package com.equator.linker.util;

import jakarta.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

public class IpNetUtil {
    /**
     * 点分十进制IP转整数
     * ip地址是由32位二进制数组成，分成四组，那每组就是8位二进制，整体就相当于一个256进制的4位数。
     *
     * @param input
     * @return
     */
    public static long dottedDecimalNotationToNumber(String input) {
        String[] nums = input.split("\\.");
        long res = 0;
        for (int i = 0; i < 4; i++) {
            res = res * 256 + Integer.parseInt(nums[i]);
        }
        return res;
    }

    /**
     * 整数转点分十进制
     *
     * @param input
     * @return
     */
    public static String numberToDottedDecimalNotation(Long input) {
        long num = input;
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            res.insert(0, num % 256 + ".");
            num /= 256;
        }
        return res.substring(0, res.length() - 1);
    }

    /**
     * 从域名获取IP
     *
     * @param domain
     * @return
     */
    public static Set<String> getIpsFromDomain(String domain) {
        Set<String> ipSets = new HashSet<>();
        try {
            InetAddress[] addresses = InetAddress.getAllByName(domain);
            for (int i = 0; i < addresses.length; i++) {
                ipSets.add(addresses[i].getHostAddress());
            }
            return ipSets;
        } catch (UnknownHostException e) {
            return ipSets;
        }
    }

    /**
     * 获取请求真实IP
     *
     * @param request
     * @return
     */
    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-real-ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
