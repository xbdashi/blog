package com.yy.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("blog.qq.email")
@Component
@Data
public class EmailProperties {
    private String sendEmail; // 发件人的邮箱地址
    private String code; // 授权码
    private String host; // qq邮箱地址
    private String serviceAddress; // qq服务地址
}
