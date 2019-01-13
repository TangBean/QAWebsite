package com.demo.controller;

import com.demo.async.EventModel;
import com.demo.async.EventProducer;
import com.demo.async.EventType;
import com.demo.service.UserService;
import com.sun.deploy.net.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

    @RequestMapping(path = {"/reglogin"}, method = RequestMethod.GET)
    public String registerPage(Model model, @RequestParam(value = "next", required = false) String next) {
        model.addAttribute("next", next);
        return "login";
    }

    @RequestMapping(path = {"/reg"}, method = RequestMethod.POST)
    public String register(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value = "remember_me", defaultValue = "false") boolean rememberMe,
                           HttpServletResponse httpServletResponse) {
        try {
            Map<String, String> msg = userService.register(username, password);
            if (msg.containsKey("ticket")) {
                model.addAttribute("ticket", msg.get("ticket"));

                Cookie cookie = new Cookie("ticket", msg.get("ticket"));
                cookie.setPath("/");
                if (rememberMe) {
                    cookie.setMaxAge(1000 * 3600);
                }
                httpServletResponse.addCookie(cookie);

                return "redirect:/";
            } else {
                model.addAttribute("msg", msg.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            logger.error("注册异常: " + e.getMessage());
            model.addAttribute("msg", "服务器错误");
            return "login";
        }
    }

    @RequestMapping(path = {"/login"}, method = RequestMethod.POST)
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "remember_me", defaultValue = "false") boolean rememberMe,
                        @RequestParam(value = "next", required = false) String next,
                        HttpServletResponse httpServletResponse) {
        try {
            Map<String, String> msg = userService.login(username, password);
            if (msg.containsKey("ticket")) {
                model.addAttribute("ticket", msg.get("ticket"));

                Cookie cookie = new Cookie("ticket", msg.get("ticket"));
                cookie.setPath("/");
                if (rememberMe) {
                    cookie.setMaxAge(1000 * 3600);
                }
                httpServletResponse.addCookie(cookie);

                // 发送登录邮件
                eventProducer.fireEvent(new EventModel().setEventType(EventType.LOGIN)
                        .setActorId(Integer.parseInt(msg.get("userId")))
                        .setExts("email", "984354469@qq.com")
                        .setExts("subject", "登录提醒")
                        .setExts("templateName", "mail/loginMailTemp.ftl")
                        .setExts("username", username));

                if (!StringUtils.isEmpty(next)) {
                    return "redirect:" + next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", msg.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            logger.error("登录异常: " + e.getMessage());
            model.addAttribute("msg", "服务器异常");
            return "login";
        }
    }
}
