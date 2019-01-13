package com.demo.controller;

import com.demo.model.*;
import com.demo.service.FollowService;
import com.demo.service.QuestionService;
import com.demo.service.SearchService;
import com.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private SearchService searchService;

    @Autowired
    private FollowService followService;

    @RequestMapping(path = {"/search"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String search(@RequestParam("q") String keyword,
                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "limit", defaultValue = "10") int limit,
                         Model model) {
        List<Question> questionList = searchService.search(keyword, offset, limit, "<strong>", "</strong>");
        List<ViewObject> vos = getQuestions(questionList);
        model.addAttribute("vos", vos);
        model.addAttribute("keyword", keyword);
        return "result";
    }

    private List<ViewObject> getQuestions(List<Question> questionList) {
        ArrayList<ViewObject> viewObjectList = new ArrayList<>();
        for (Question q : questionList) {
            ViewObject object = new ViewObject();
            object.set("question", q);
            User user = userService.getById(q.getUserId());
            object.set("user", user);
//            int followerCount = followService.getFollowerCount(EntityType.ENTITY_QUESTION, q.getId());
            object.set("followerCount", 4);
            viewObjectList.add(object);
        }
        return viewObjectList;
    }
}
