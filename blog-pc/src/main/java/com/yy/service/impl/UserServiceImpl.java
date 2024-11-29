package com.yy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yy.pojo.User;
import com.yy.service.UserService;
import com.yy.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author Max
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-11-25 16:30:53
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




