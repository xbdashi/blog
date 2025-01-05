package com.yy.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties("blog.jwt")
@Component
@Data
public class JwtProperties {
    private String secret;
    private Long expire;
}
