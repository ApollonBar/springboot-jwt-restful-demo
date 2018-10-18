package cn.jerryshell.jwttest.interceptor;

import cn.jerryshell.jwttest.annotation.TokenRequired;
import cn.jerryshell.jwttest.util.JWTUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 判断 handler 是否有 TokenRequired 注解，如果没有则拦截器直接放行
        boolean isTokenRequired = ((HandlerMethod) handler).hasMethodAnnotation(TokenRequired.class);
        if (!isTokenRequired) {
            return true;
        }
        // 校验 token
        String token = request.getHeader("Authorization");
        String username = JWTUtil.getUsername(token);
        String role = JWTUtil.getRole(token);
        boolean verify = JWTUtil.verify(token, username, role);
        if (verify) {
            // 验证通过，在请求域设置相应的属性
            request.setAttribute("username", username);
            request.setAttribute("role", role);
            return true;
        }
        return false;
    }
}
