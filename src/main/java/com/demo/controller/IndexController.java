package com.demo.controller;

import com.demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

//@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(path = {"/"}, method = RequestMethod.GET)
//    @ResponseBody
    public String index(Model model) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
//            users.add(new User("User" + String.valueOf(i + 1)));
        }
        model.addAttribute("users", users);
        logger.info("index");
        return "home";
    }
}
