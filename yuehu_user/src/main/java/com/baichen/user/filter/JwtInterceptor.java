package com.baichen.user.filter;

import com.baichen.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

/**
 * @Program: JwtFilter
 * @Author: baichen
 * @Description: 登录拦截器，负责把请求头中含有token的令牌进行一个解析验证
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtil jwtUtil;

    //    在所有controller 方法执行前执行此方法
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String authHeader = request.getHeader("Authorization");   // 请求头,作为key，下面的Bearer +token作为value
        if (authHeader != null) {
            if (authHeader.startsWith("Bearer ")) {
                final String token = authHeader.substring(7); // 获取与"Bearer "拼接的token,即提取token
                Claims claims = jwtUtil.parseJWT(token);
                if (claims != null) {
                    if ("admin".equals(claims.get("roles"))) {  //如果是管理员
                        request.setAttribute("admin_claims", claims);
                    }
                    if ("user".equals(claims.get("roles"))) {   //如果是用户
                        request.setAttribute("user_claims", claims);
                    }
                }
            }
        }
        return true;    // 请求头为空并且不包含Authorization则放行
    }
}
