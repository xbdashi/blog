package com.yy.utils;

public class UserContext {
    // 使用ThreadLocal来存储用户的ID
    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

    // 设置用户的ID
    public static void setUserId(Long userId) {
        userIdHolder.set(userId);
    }

    // 获取用户的ID
    public static Long getUserId() {
        return userIdHolder.get();
    }

    // 清除用户的ID，通常在请求结束时调用
    public static void clearUserId() {
        userIdHolder.remove();
    }
}
