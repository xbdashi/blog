package com.yy.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
public class JwtUtils {
    private String secret; // 密钥
    private Long expire; // 过期时间

    public String createJwt(Long id) {
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.MILLISECOND, expire.intValue());

        Map<String,Object> payload = new HashMap<String,Object>();
        //签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        //过期时间
        payload.put(JWTPayload.EXPIRES_AT, newTime);
        //生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);
        //载荷 把用户id存入
        payload.put("id", id);
        return JWTUtil.createToken(payload, secret.getBytes());
    }

    public Long validateJwt(String token){
        try {
            boolean verify = JWTUtil.verify(token, secret.getBytes());
            if (verify) {
                return Long.valueOf(JWTUtil.parseToken(token).getPayload("id").toString());
            } else {
                throw new RuntimeException("token无效");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
