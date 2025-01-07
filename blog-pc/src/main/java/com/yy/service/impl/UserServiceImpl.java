package com.yy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yy.enums.UserStatus;
import com.yy.exception.AccountException;
import com.yy.exception.CodeException;
import com.yy.pojo.User;
import com.yy.pojo.dto.LoginDto;
import com.yy.service.UserService;
import com.yy.mapper.UserMapper;
import com.yy.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
* @author Max
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-11-25 16:30:53
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ValueOperations<String,Object> valueOperations;
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public Map<String,Object> login(LoginDto loginDto, HttpSession httpSession) {
        System.out.println(jwtUtils);
        // 1. 查询是否有当前的账号，如果有就登录，如果没有就是插入用户
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getUsername,loginDto.getUsername());
        User user = userMapper.selectOne(qw);
        String code = httpSession.getAttribute("code")+"";
        // 如果状态为0则是被冻结
        if(Objects.equals(user.getStatus(), UserStatus.LOCKED.getCode())){
            throw new AccountException(400,"账号已被冻结");
        }
        if(!code.equalsIgnoreCase(loginDto.getCode())){
            throw new CodeException(400,"验证码错误");
        }
        String token = "";

        if(user == null || !StrUtil.isBlank(loginDto.getEmailCode())){ // 插入用户
            User newUser = User.builder()
                    .username(loginDto.getUsername())
                    .password(PasswordUtil.encrypt(loginDto.getPassword()))
                    .isLogin(1L) // 1为已经登录
                    .email(loginDto.getEmail())
                    .build();
            // 检查邮箱
            checkEmailCode(loginDto);
            // 判断邮箱和账号是否已经被注册
            checkIsExists(loginDto);
            userMapper.insert(newUser);
            user = userMapper.selectById(newUser.getId());
        }else{ // 登录
            checkUser(user,loginDto,httpSession);
        }
        // 修改用户未登录的状态
        updateUserStatus(user.getId(), UserStatus.LOGIN.getCode());
        UserContext.setUserId(user.getId());
        Map<String,Object> map = new HashMap<>();
        map.put("token",jwtUtils.createJwt(user.getId()));
        map.put("userinfo",user);
        return map;
    }

    // 修改用户状态
    public void updateUserStatus(Long userId,Long status) {
        User user1 = new User();
        user1.setIsLogin(status);
        user1.setId(userId);
        this.baseMapper.updateById(user1);
    }

    private void checkIsExists(LoginDto loginDto) {
        if(userMapper.selectCount(new QueryWrapper<User>().lambda().eq(User::getUsername,loginDto.getUsername())) > 0){
            throw new AccountException(400,"用户名已存在");
        }
        if(userMapper.selectCount(new QueryWrapper<User>().lambda().eq(User::getEmail,loginDto.getEmail())) > 0){
            throw new AccountException(400,"邮箱已存在");
        }
    }

    // 检查用户的所有
    private void checkUser(User user,LoginDto loginDto, HttpSession httpSession) {
        if(!user.getUsername().equals(loginDto.getUsername())){
            throw new AccountException(400,"用户名不存在");
        }
        if(!PasswordUtil.matches(loginDto.getPassword(),user.getPassword())){
            throw new AccountException(400,"密码错误");
        }

    }

    @Override
    public Result resetPwd(LoginDto loginDto) {
        // 通过邮箱获取到用户
        User user = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getEmail,loginDto.getEmail()));
        if(user == null){
            throw new AccountException(400,"邮箱不存在");
        }
        // 从redis获取到验证码
        checkEmailCode(loginDto);
        user.setPassword(PasswordUtil.encrypt(loginDto.getPassword()));
        this.baseMapper.updateById(user);
        return Result.success(200,"密码重置成功,你的账号为"+user.getUsername(),null);
    }

    private void checkEmailCode(LoginDto loginDto) {
        Object o = valueOperations.get(loginDto.getEmail());
        if(Objects.isNull(o)){
            throw new CodeException(400,"邮箱已过期，请重新发送");
        }else if(!Objects.equals(o.toString(), loginDto.getEmailCode())){
            throw new CodeException(400,"邮箱验证码错误");
        }
    }
}




