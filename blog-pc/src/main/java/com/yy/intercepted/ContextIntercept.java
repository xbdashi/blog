package com.yy.intercepted;

import cn.hutool.core.util.StrUtil;
import com.yy.exception.JwtException;
import com.yy.utils.JwtUtils;
import com.yy.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class ContextIntercept implements HandlerInterceptor {
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if(!StrUtil.isBlank(token)){
            System.out.println(jwtUtils);
            try {
                Long l = jwtUtils.validateJwt(token);
                UserContext.setUserId(l) ; // 当前用户的id
            } catch (Exception e) {
                throw new JwtException(401,"身份过期请重新登录....");
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clearUserId();
    }
}
