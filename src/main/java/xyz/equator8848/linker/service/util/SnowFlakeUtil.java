package xyz.equator8848.linker.service.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class SnowFlakeUtil {
    private static final Snowflake snowflake = IdUtil.getSnowflake();

    /**
     * 生成long 类型的ID
     *
     * @return {@link Long}
     */
    public static Long getId() {
        return snowflake.nextId();
    }

    /**
     * 生成String 类型的ID
     *
     * @return {@link String}
     */
    public static String getIdStr() {
        return snowflake.nextIdStr();
    }


}

