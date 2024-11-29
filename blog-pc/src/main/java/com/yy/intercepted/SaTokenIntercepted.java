package com.yy.intercepted;

import cn.dev33.satoken.stp.StpUtil;
import com.yy.exception.IdentifyException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class SaTokenIntercepted implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截器执行了");
        // 获取前端传来的cookie值
        String token = request.getHeader("token");
        // 前端传来的token和后端的token进行比较
        if(!StpUtil.getTokenValue().equals(token)){

            throw new IdentifyException(401, "身份过期请重新登录");
        }
        if(StpUtil.getTokenInfo().getTokenTimeout() < 0){

            throw new IdentifyException(401, "身份过期请重新登录");
        }
        return true;
    }
}
