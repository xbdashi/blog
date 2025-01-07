package com.yy.controller.common;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.Producer;
import com.yy.exception.EmailException;
import com.yy.pojo.User;
import com.yy.pojo.dto.LoginDto;
import com.yy.service.UserService;
import com.yy.utils.EmailUtil;
import com.yy.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Api(tags = "获取验证码")
@Slf4j
public class CommontController {
    @Autowired
    private Producer producer;
    @Autowired
    private FileStorageService fileStorageService;//注入实列
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private UserService userService;
    @GetMapping("/code")
    @ApiOperation("获取验证码")
    // knif4j将这两个参数标记为不是参数
    public Result getKaptcha(@ApiParam(hidden = true) HttpServletResponse response, @ApiParam(hidden = true) HttpSession session) throws IOException, InterruptedException {
        String text = producer.createText();
        session.setAttribute("code",text);
        BufferedImage image = producer.createImage(text);
        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
        ImageIO.write(image,"jpg",fos);
        return Result.success("data:image/png;base64,"+Base64.encodeBase64String(fos.toByteArray()));
    }
    /**
     * 上传文件，成功返回文件 url
     */
    @PostMapping("/upload")
    public String upload2(MultipartFile file) {
        // 获取当天的时间作为文件夹 以便存储到oss好区分
        String year = DateUtil.format(new Date(), "yyyy");
        String month = DateUtil.format(new Date(), "MM");
        String day = DateUtil.format(new Date(), "dd");
        // 设置文件名
        String name = UUID.randomUUID() +file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        FileInfo fileInfo = fileStorageService.of(file)
                .setPath(year+"/"+month+"/"+day+"/") //保存到相对路径下，为了方便管理，不需要可以不写
                .setSaveFilename(name) //设置保存的文件名，不需要可以不写，会随机生成
                .upload();  //将文件上传到对应地方
        return fileInfo == null ? "上传失败！" : fileInfo.getUrl();
    }
    @PostMapping("/email")
    public Result getEmailCode(@RequestBody LoginDto loginDto) {
        log.info("发送的邮箱{}",loginDto);
        // 通过邮箱查询用户，如果有在发送
        QueryWrapper<User> wq = new QueryWrapper<>();
        wq.lambda().eq(User::getEmail,loginDto.getEmail());
        User one = userService.getOne(wq);
        if(one == null){
            throw new EmailException(411,"邮箱没有绑定任何用户");
        }
        // 存入redis 5分钟过期
        emailUtil.send(loginDto.getEmail());
        return Result.success(200,"发送成功",null);
    }
}
