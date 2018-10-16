package cn.jerryshell.jwttest.interceptor;

import cn.jerryshell.jwttest.annotation.TokenRequired;
import cn.jerryshell.jwttest.util.JWTUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 判断接口是否需要登录
        TokenRequired methodAnnotation = method.getAnnotation(TokenRequired.class);
        if (methodAnnotation == null) {
            return true;
        }

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
