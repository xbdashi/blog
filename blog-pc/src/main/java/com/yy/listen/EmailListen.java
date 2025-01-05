package com.yy.listen;

import com.yy.exception.EmailException;
import com.yy.utils.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailListen {
    @Autowired
    private EmailUtil emailUtil;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "blog.email", durable = "true"),
            exchange = @Exchange(value = "email.exchange", type = "direct"),
            key = "email"
    ))
    public void listen(String email) {
        log.info("监听到消息：{}", email);
        try {
            emailUtil.send(email);
        } catch (Exception e) {
            log.error("邮件发送失败，请检查邮件输入正确，或者联系管理员");
            throw new EmailException(530,"邮件发送失败，请检查邮件输入正确，或者联系管理员");
        }
    }
}
