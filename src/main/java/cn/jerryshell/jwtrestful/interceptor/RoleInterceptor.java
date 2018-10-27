package cn.jerryshell.jwtrestful.interceptor;

import cn.jerryshell.jwtrestful.annotation.RoleRequired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RoleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果 handler 不是 HandlerMethod 实例直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 如果 handler 没有 RoleRequired 注解直接放行
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RoleRequired annotation = handlerMethod.getMethodAnnotation(RoleRequired.class);
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
