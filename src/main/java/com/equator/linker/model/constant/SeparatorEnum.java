package com.equator.linker.model.constant;

public enum SeparatorEnum {
    SLASH("/"),
    COLON(":");

    private final String separator;

    public String getSeparator() {
        return separator;
    }

    SeparatorEnum(String separator) {
        this.separator = separator;
    }
}
