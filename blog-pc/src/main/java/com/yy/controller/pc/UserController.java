package com.yy.controller.pc;

import com.yy.pojo.Relation;
import com.yy.pojo.User;
import com.yy.pojo.dto.FocusDto;
import com.yy.service.RelationService;
import com.yy.service.UserService;
import com.yy.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@Api(tags = "用户接口")
public class UserController {
    @Autowired
    private UserService userService;
    // 根据id查询该用户
    @Autowired
    private RelationService relationService;
    @GetMapping("{id}")
    @ApiOperation(value = "根据id查询用户")
    public Result<User> selectById(@PathVariable Long id){
        return Result.success(userService.getById(id));
    }

    @PostMapping("focus")
    public Result focus(@RequestBody Relation relation){
        return Result.success(relationService.save(relation));
    }
}
