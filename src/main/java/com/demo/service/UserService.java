package com.demo.service;

import com.demo.dao.LoginTicketDao;
import com.demo.dao.UserDao;
import com.demo.model.LoginTicket;
import com.demo.model.User;
import com.demo.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.rmi.runtime.Log;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTicketDao loginTicketDao;

    /**
     * 注册用户
     *
     * @param username
     * @param password
     * @return
     */
    public Map<String, String> register(String username, String password) {
        Map<String, String> messageMap = new HashMap<>();
        if (username == null || username.trim().length() == 0) {
            messageMap.put("msg", "用户名不能为空");
        } else if (password == null || password.trim().length() == 0) {
            messageMap.put("msg", "密码不能为空");
        } else if (password.length() < 8) {
            messageMap.put("msg", "密码太短，应大于等于8位");
        } else if (userDao.searchByName(username) != null) {
            messageMap.put("msg", "该用户名已被注册");
        } else {
            String salt = UUID.randomUUID().toString().substring(0, 8);
            String md5Password = WendaUtil.MD5(password + salt);
            String headUrl = "http://images.nowcoder.com/head/" + new Random().nextInt(1000) + "t.png";
            User user = new User(username, md5Password, salt, headUrl);
            userDao.addUser(user);

            String ticket = addLoginTicket(user.getId());
            messageMap.put("ticket", ticket);
        }
        return messageMap;
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    public Map<String, String> login(String username, String password) {
        Map<String, String> messageMap = new HashMap<>();
        if (username == null || username.trim().length() == 0) {
            messageMap.put("msg", "用户名不能为空");
            return messageMap;
        }

        if (password == null || password.trim().length() == 0) {
            messageMap.put("msg", "密码不能为空");
            return messageMap;
        }

        User user = userDao.searchByName(username);

        if (user == null) {
            messageMap.put("msg", "该用户尚未注册，请先注册");
            return messageMap;
        }

        String passwordWithSalt = WendaUtil.MD5(password + user.getSalt());
        if (!passwordWithSalt.equals(user.getPassword())) {
            messageMap.put("msg", "密码错误！");
            return messageMap;
        }

        String ticket = addLoginTicket(user.getId());
        messageMap.put("ticket", ticket);
        messageMap.put("userId", String.valueOf(user.getId()));
        return messageMap;
    }

    /**
     * 登出
     * @return
     */
    public void logout(String ticket) {
        loginTicketDao.updateStatus(ticket, 1);
    }

    /**
     * 添加LoginTicket
     * @param userId
     * @return
     */
    private String addLoginTicket(int userId) {
        String ticket = UUID.randomUUID().toString().replace("-", "");
        Date expired = new Date();
        expired.setTime(expired.getTime() + 1000 * 3600);
        LoginTicket loginTicket = new LoginTicket(userId, ticket, expired);
        loginTicketDao.addTicket(loginTicket);
        return ticket;
    }

    /**
     * 通过id获取用户
     * @param userId
     * @return
     */
    public User getById(int userId) {
        return userDao.searchById(userId);
    }

    public User getBName(String name) {
        return userDao.searchByName(name);
    }
}
