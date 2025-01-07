package com.yy.service;

import com.yy.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yy.pojo.dto.LoginDto;
import com.yy.utils.Result;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
* @author Max
* @description 针对表【user】的数据库操作Service
* @createDate 2024-11-25 16:30:53
*/
public interface UserService extends IService<User> {

    Map<String,Object> login(LoginDto loginDto, HttpSession httpSession);

    Result resetPwd(LoginDto loginDto);
    // 修改用户状态
    void updateUserStatus(Long userId,Long status);
}
