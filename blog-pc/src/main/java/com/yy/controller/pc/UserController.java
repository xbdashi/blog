package com.yy.controller.pc;

import com.yy.pojo.User;
import com.yy.service.UserService;
import com.yy.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@Api(tags = "用户接口")
public class UserController {
    @Autowired
    private UserService userService;
    // 根据id查询该用户

    @GetMapping("{id}")
    @ApiOperation(value = "根据id查询用户")
    public Result<User> selectById(@PathVariable Long id){
        return Result.success(userService.getById(id));
    }
}
