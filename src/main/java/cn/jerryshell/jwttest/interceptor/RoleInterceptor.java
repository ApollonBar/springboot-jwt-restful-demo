package cn.jerryshell.jwttest.interceptor;

import cn.jerryshell.jwttest.annotation.RoleRequired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RoleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 判断 handler 是否有 RoleRequired 注解，如果没有则拦截器直接放行
        RoleRequired annotation = ((HandlerMethod) handler).getMethodAnnotation(RoleRequired.class);
        if (annotation == null) {
            return true;
        }
        // 校验 role
        String role = (String) request.getAttribute("role");
        String[] roles = annotation.roles();
        for (String s : roles) {
            if (s.equals(role)) {
                return true;
            }
        }
        return false;
    }
}
