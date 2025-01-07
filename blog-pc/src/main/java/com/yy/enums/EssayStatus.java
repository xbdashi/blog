package com.yy.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EssayStatus {

    SAVED(0L, "已保存"),
    REVIEWING(1L, "审核中"),
    PUBLISHED(2L, "已发布"),
    NOT_PUBLISHED(3L, "未通过");

    @JsonValue
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
