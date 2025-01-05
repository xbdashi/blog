package com.yy;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.yy.utils.JwtUtils;
import com.yy.utils.PasswordUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class TestImg {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ValueOperations<String, Object> valueOperations;
    @Autowired
    private JwtUtils jwtUtils;
    @Test
    // 测试邮件发送
    public void test(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2893127039@qq.com"); // 发件人邮箱
        message.setTo("1213454634@qq.com");                   // 收件人邮箱
        message.setSubject("测试测试");         // 邮件主题
        message.setText("123123");            // 邮件内容
        javaMailSender.send(message);
    }

    @Test
    // 测试redis
    public void test2(){
        valueOperations.set("ts","ts");
    }
    // 测试加密解密
    @Test
    public void test3(){
        String encrypt = PasswordUtil.encrypt("123");
        System.out.println(encrypt);
    }
    //JWT
    @Test
    public void test4(){
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.MILLISECOND, 10);

        Map<String,Object> payload = new HashMap<String,Object>();
        //签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        //过期时间
        payload.put(JWTPayload.EXPIRES_AT, newTime);
        //生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);
        //载荷
        payload.put("userName", "zhangsan");
        payload.put("passWord", "666889");

        String key = "aabb";
        String token = JWTUtil.createToken(payload, key.getBytes());
        System.out.println(token);
    }

    @Test //测试工具类
    public void test6(){
        String jwt = jwtUtils.createJwt(1L);
        System.out.println(jwt);
    }
    @Test //测试工具类
    public void test7(){
        Long l = jwtUtils.validateJwt("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYmYiOjE3MzU5NTIyNzIsImlkIjoxLCJleHAiOjE3MzU5NTI5OTIsImlhdCI6MTczNTk1MjI3Mn0.rS509tCF8DgkK6kqx9XYLkRugObVnm6RUCK1PzogpXM");
        System.out.println(l);
    }
}
