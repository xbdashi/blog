package com.yy.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 对密码进行加密
     * @param rawPassword 明文密码
     * @return 加密后的密码
     */
    public static String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * 校验密码是否正确
     * @param rawPassword 明文密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
