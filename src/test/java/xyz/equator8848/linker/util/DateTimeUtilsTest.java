package xyz.equator8848.linker.util;

import org.junit.Test;
import xyz.equator8848.linker.service.util.DateTimeUtils;

public class DateTimeUtilsTest {
    @Test
    public void test1() {
        System.out.println(DateTimeUtils.getTimeFromISO8601("2024-02-20T13:01:31.000+08:00"));
    }
}
