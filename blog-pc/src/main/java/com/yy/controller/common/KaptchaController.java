package com.yy.controller.common;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.yy.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@Api(tags = "获取验证码")
public class KaptchaController {
    @Autowired
    private Producer producer;

    @GetMapping("/code")
    @ApiOperation("获取验证码")
    // knif4j将这两个参数标记为不是参数

    public Result getKaptcha(@ApiParam(hidden = true) HttpServletResponse response, @ApiParam(hidden = true) HttpSession session) throws IOException {
        String text = producer.createText();
        session.setAttribute("code",text);
        BufferedImage image = producer.createImage(text);
        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
        ImageIO.write(image,"jpg",fos);
        return Result.success("data:image/png;base64,"+Base64.encodeBase64String(fos.toByteArray()));
    }


}
