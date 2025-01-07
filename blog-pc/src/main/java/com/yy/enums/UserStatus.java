package com.yy.enums;

public enum UserStatus {
    NORMAL(0L, "正常"),
    LOCKED(1L, "锁定"),
    LOGIN(1L, "在线"),
    LOGOUT(0L, "离线");
    private Long code;
    private String desc;

    UserStatus(Long code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Long getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
