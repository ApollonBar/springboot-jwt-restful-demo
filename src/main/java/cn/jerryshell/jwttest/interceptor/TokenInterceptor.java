package cn.jerryshell.jwttest.interceptor;

import cn.jerryshell.jwttest.annotation.TokenRequired;
import cn.jerryshell.jwttest.util.JWTUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 判断 handler 是否有 TokenRequired 注解，如果没有则拦截器直接放行
        boolean isTokenRequired = handler.getClass().isAnnotationPresent(TokenRequired.class);
        if (!isTokenRequired) {
            return true;
        }
        // 校验 token
        String token = request.getHeader("Authorization");
        String username = JWTUtil.getUsername(token);
        boolean verify = JWTUtil.verify(token, username);
        if (verify) {
            request.setAttribute("username", username);
            return true;
        }
        return false;
    }
}
