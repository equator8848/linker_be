package com.equator.linker.model.constant;

public enum JenkinsPipelineBuildResult {
    /**
     * 未知
     */
    UNKNOWN("未知"),
    /**
     * 构建中
     */
    BUILDING("构建中"),
    /**
     * 成功
     */
    SUCCESS("成功"),

    /**
     * 失败
     */
    FAILURE("失败");


    private final String nameCn;

    JenkinsPipelineBuildResult(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameCn() {
        return nameCn;
    }
}
