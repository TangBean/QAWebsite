package com.demo.controller;

import com.demo.async.EventModel;
import com.demo.async.EventProducer;
import com.demo.async.EventType;
import com.demo.model.*;
import com.demo.service.*;
import com.demo.util.WendaUtil;
import freemarker.ext.beans.StringModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {
    private Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = {"/question/{qid}"}, method = RequestMethod.GET)
    public String detail(@PathVariable("qid") int qid, Model model) {
        Question question = questionService.selectById(qid);
        model.addAttribute("question", question);
        int followerCount = followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId());
        model.addAttribute("followerCount", followerCount);
        if (hostHolder.getUser() != null) {
            model.addAttribute("isFollowed", followService.isFollower(hostHolder.getUser().getId(),
                    EntityType.ENTITY_QUESTION, qid));
        } else {
            model.addAttribute("isFollowed", false);
        }

        List<Comment> commentList = commentService.selectCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> vos = new ArrayList<>();
        for (Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            vo.set("user", userService.getById(comment.getUserId()));
            vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), comment.getId(), EntityType.ENTITY_COMMENT));
            vo.set("likeCount", likeService.getLikeCount(comment.getId(), EntityType.ENTITY_COMMENT));
            vos.add(vo);
        }
        model.addAttribute("commentVos", vos);

        return "detail";
    }

    @RequestMapping(path = {"/question/add"}, method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, String content, Model model) {
        try {
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            if (hostHolder.getUser() != null) {
                question.setUserId(hostHolder.getUser().getId());
            } else {
                question.setUserId(WendaUtil.ANONYMOUS_USERID);
            }

            if (questionService.addQuestion(question) > 0) {
                eventProducer.fireEvent(new EventModel().setEventType(EventType.QUESTION)
                        .setExts("qid", String.valueOf(question.getId()))
                        .setExts("title", question.getTitle())
                        .setExts("content", question.getContent()));
                return WendaUtil.getJSONString(0, "成功");
            }
        } catch (Exception e) {
            logger.error("添加问题失败" + e.getMessage());
        }
        return WendaUtil.getJSONString(1, "失败");
    }
}
