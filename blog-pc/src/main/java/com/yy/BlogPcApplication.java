package com.yy;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFileStorage // 开启文件存储功能
public class BlogPcApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogPcApplication.class, args);
    }
}
