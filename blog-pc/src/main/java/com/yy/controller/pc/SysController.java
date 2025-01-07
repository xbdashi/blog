package com.yy.controller.pc;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yy.enums.UserStatus;
import com.yy.exception.AccountException;
import com.yy.exception.CodeException;
import com.yy.pojo.User;
import com.yy.pojo.dto.LoginDto;
import com.yy.service.UserService;
import com.yy.utils.JwtUtils;
import com.yy.utils.Result;
import com.yy.utils.UserContext;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sys")
public class SysController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @PostMapping("/login")
    public Result login(@RequestBody @Validated LoginDto loginDto, HttpSession httpSession){
        return Result.success(200,"登录成功",userService.login(loginDto,httpSession));
    }

    @PostMapping("/resetPwd")
    public Result resetPwd(@RequestBody LoginDto loginDto){
        return userService.resetPwd(loginDto);
    }

    @GetMapping("{id}")
    public Result logout(@PathVariable Long id){
        userService.updateUserStatus(id, UserStatus.LOGOUT.getCode());
        return Result.success();
    }
}
