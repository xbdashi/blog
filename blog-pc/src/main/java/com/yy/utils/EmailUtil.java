package com.yy.utils;

import com.yy.exception.EmailException;
import com.yy.properties.EmailProperties;
import lombok.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedRuntimeException;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@Data
@Builder
@Component
public class EmailUtil {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ValueOperations<String,Object> valueOperations;


    public void send(String receptionEmail){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("2893127039@qq.com"); // 发件人邮箱
            message.setTo(receptionEmail);                   // 收件人邮箱
            message.setSubject("验证码（5分钟后过期）");         // 邮件主题
            String code = createCheck(5);
            message.setText(code);            // 邮件内容
            // 存入redis中 5分钟过期
            valueOperations.set(receptionEmail,code,3, TimeUnit.MINUTES);
            javaMailSender.send(message);
        } catch (MailSendException e) {
            // 捕获通用的邮件发送异常
            throw new EmailException(500,"邮件发送失败,请检查邮箱是否正确，或联系管理员！");
        }catch(NestedRuntimeException e){
            throw new EmailException(500,"邮件发送失败,请检查邮箱是否正确，或联系管理员！");
        } catch (Exception e) {
            // 捕获其他可能的异常
            throw new EmailException(500,"邮件发送失败,请检查邮箱是否正确，或联系管理员！");
        }
    }


    private String createCheck(int k) {
        // 生成的位数
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < k; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
