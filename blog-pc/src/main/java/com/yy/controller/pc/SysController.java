package com.yy.controller.pc;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yy.exception.AccountException;
import com.yy.exception.CodeException;
import com.yy.pojo.User;
import com.yy.pojo.dto.LoginDto;
import com.yy.service.UserService;
import com.yy.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController

public class SysController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public Result login(@RequestBody LoginDto loginDto, HttpSession httpSession){
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getUsername,loginDto.getUsername());
        User user = userService.getOne(qw);
        if (user==null){
            throw new AccountException(400,"账号或密码错误");
        }
        if (!user.getPassword().equals(loginDto.getPassword())){
            throw new AccountException(400,"账号或密码错误");
        }
        System.out.println(httpSession.getAttribute("code"));
        if (!httpSession.getAttribute("code").equals(loginDto.getCode())) {
            throw new CodeException(400,"验证码错误");
        }
        StpUtil.login(user.getId());
        // System.out.println(StpUtil.getTokenInfo().getTokenTimeout());
        Map<String,Object> map = new HashMap<>();
        map.put("user",user);
        map.put("token",StpUtil.getTokenValue());
        return Result.success(200,"登录成功",map);
    }
}
