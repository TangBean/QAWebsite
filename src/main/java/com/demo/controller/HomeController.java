package com.demo.controller;

import com.demo.model.EntityType;
import com.demo.model.Question;
import com.demo.model.User;
import com.demo.model.ViewObject;
import com.demo.service.FollowService;
import com.demo.service.QuestionService;
import com.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @RequestMapping(path = {"/user/{userId}"}, method = RequestMethod.GET)
    public String userIndex(Model model, @PathVariable("userId") String userId) {
        List<ViewObject> viewObjects = getQuestions(Integer.valueOf(userId), 0, 10);
        model.addAttribute("vos", viewObjects);
        return "index";
    }

    @RequestMapping(path = {"/", "/home"})
    public String index(Model model) {
        List<ViewObject> viewObjects = getQuestions(0, 0, 10);
        model.addAttribute("vos", viewObjects);
        return "index";
    }

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        ArrayList<ViewObject> viewObjectList = new ArrayList<>();
        List<Question> questionList = questionService.selectLatestQuestions(offset, limit);
        for (Question q : questionList) {
            ViewObject object = new ViewObject();
            object.set("question", q);
            User user = userService.getById(q.getUserId());
            object.set("user", user);
            int followerCount = followService.getFollowerCount(EntityType.ENTITY_QUESTION, q.getId());
            object.set("followerCount", followerCount);
            viewObjectList.add(object);
        }
        return viewObjectList;
    }
}
