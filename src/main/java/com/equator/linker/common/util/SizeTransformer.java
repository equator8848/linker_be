package com.equator.linker.common.util;

public class SizeTransformer {
    /**
     * X实际值，Y设置值
     * X*1024*1024*1024=Y*1000*1000*1000
     * 输入X，求Y
     *
     * @param targetSize
     * @return sourceSize
     */
    public static int getSizeFrom1024(int targetSize) {
        // return (targetSize * 1024 * 1024 * 1024) / (1000 * 1000 * 1000);
        return (int) Math.ceil(targetSize * 1.073741824);
    }
}
