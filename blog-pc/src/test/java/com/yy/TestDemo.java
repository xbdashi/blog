package com.yy;

import cn.dev33.satoken.stp.StpUtil;
import com.google.code.kaptcha.Producer;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class TestDemo {
    @Autowired
    private Producer defaultKaptcha;
    @Test
    public void codeByBase64() {
        String kaptchaText = defaultKaptcha.createText(); // 生成文本
        BufferedImage image = defaultKaptcha.createImage(kaptchaText); // 生成图片

        String base64Code = "";
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outputStream);
            base64Code = Base64.encodeBase64String(outputStream.toByteArray());
        } catch (Exception e) {
            System.out.println("verificationCode exception: ");
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    System.out.println("verificationCode outputStream close exception: ");
                }
            }
        }

        System.out.println("data:image/jpeg;base64,"+base64Code.replaceAll("\r\n",""));
    }

    @Test
    public void getTime(){
        System.out.println(StpUtil.getTokenInfo());
    }
}
