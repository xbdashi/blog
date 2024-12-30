package com.yy.enums;

public enum EssayStatus {

    SAVED(0L, "已保存"),
    REVIEWING(1L, "审核中"),
    PUBLISHED(2L, "已发布");

    private final Long code;
    private final String value;

    EssayStatus(Long code, String value) {
        this.code = code;
        this.value = value;
    }

    public Long getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
