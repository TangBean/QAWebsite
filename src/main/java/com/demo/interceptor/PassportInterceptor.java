package com.demo.interceptor;

import com.demo.dao.LoginTicketDao;
import com.demo.dao.UserDao;
import com.demo.model.HostHolder;
import com.demo.model.LoginTicket;
import com.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportInterceptor implements HandlerInterceptor {
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LoginTicketDao loginTicketDao;

    @Autowired
    private UserDao userDao;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        // 从cookie中获取ticket
        Cookie[] cookies = httpServletRequest.getCookies();
        String ticket = null;
        // 记得检查cookie是否为null
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                }
            }
        }

        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
            if (loginTicket == null
                    || loginTicket.getExpired().before(new Date())
                    || loginTicket.getStatus() != 0) {
                return true;
            }
            User user = userDao.searchById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        // 如果ModelAndView不为空，把user加入到ModelAndView模板中
        if (modelAndView != null) {
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        // 请求结束后，把当前线程的user从hostHolder中删除
        hostHolder.clear();
    }
}
