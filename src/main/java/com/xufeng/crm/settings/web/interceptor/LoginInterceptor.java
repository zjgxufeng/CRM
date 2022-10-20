package com.xufeng.crm.settings.web.interceptor;

import com.xufeng.crm.settings.contants.Contants;
import com.xufeng.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //用户访问其他资源时,看看是否已经登录
        HttpSession session = httpServletRequest.getSession();
       User user = (User) session.getAttribute(Contants.SESSION_USER);
       if (user==null){
           httpServletResponse.sendRedirect(httpServletRequest.getContextPath());
           return false;
       }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
