package com.demo.controller;

import com.demo.model.*;
import com.demo.service.CommentService;
import com.demo.service.FollowService;
import com.demo.service.QuestionService;
import com.demo.service.UserService;
import com.demo.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FollowController {
    private final Logger logger = LoggerFactory.getLogger(FollowController.class);

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private QuestionService questionService;

    /**
     * 访问用户所有关注的用户列表
     */
    @RequestMapping(path = {"/user/{uid}/followees"}, method = RequestMethod.GET)
    public String followees(@PathVariable("uid") int uid, Model model) {
        try {
            List<Integer> followeeIds = followService.getFollowees(uid, EntityType.ENTITY_USER, 10);
            if (hostHolder.getUser() != null) {
                model.addAttribute("userInfos", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
            } else {
                model.addAttribute("userInfos", getUsersInfo(0, followeeIds));
            }
            model.addAttribute("curUserName", userService.getById(uid).getName());
            model.addAttribute("curFolloweeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            return "followee";
        } catch (Exception e) {
            logger.error("访问关注者列表出错：" + e.getMessage());
        }
        return null;
    }

    /**
     * 访问用户所有的粉丝列表
     */
    @RequestMapping(path = {"/user/{uid}/followers"}, method = RequestMethod.GET)
    public String followers(@PathVariable("uid") int uid, Model model) {
        try {
            List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, uid, 10);
            if (hostHolder.getUser() != null) {
                model.addAttribute("userInfos", getUsersInfo(hostHolder.getUser().getId(), followerIds));
            } else {
                model.addAttribute("userInfos", getUsersInfo(0, followerIds));
            }
            model.addAttribute("curUserName", userService.getById(uid).getName());
            model.addAttribute("curFollowerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            return "follower";
        } catch (Exception e) {
            logger.error("访问粉丝列表出错：" + e.getMessage());
        }
        return null;
    }

    /**
     * 根据用户的id列表获取用来显示的vo列表
     */
    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<>();
        for (int uid : userIds) {
            User user = userService.getById(uid);
            // 要检查以下该uid是否存在!
            if (user == null) {
                continue;
            }

            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            vo.set("commentCount", commentService.getUserCommentCount(uid));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }

    /**
     * 访问用户所有关注的问题列表
     */
    @RequestMapping(path = {"/question/{uid}/followees"}, method = RequestMethod.GET)
    public String questionFollowees(@PathVariable("uid") int uid, Model model) {
        try {
            List<Integer> questionFolloweeIds = followService.getFollowees(uid, EntityType.ENTITY_QUESTION, 10);
            if (hostHolder.getUser() != null) {
                model.addAttribute("questionInfos", getQuestionsInfo(hostHolder.getUser().getId(), questionFolloweeIds));
            } else {
                model.addAttribute("questionInfos", getQuestionsInfo(0, questionFolloweeIds));
            }
            model.addAttribute("curUserName", userService.getById(uid).getName());
            model.addAttribute("curQuestionFolloweeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_QUESTION));
            return "questionFollowee";
        } catch (Exception e) {
            logger.error("访问用户关注的问题列表出错：" + e.getMessage());
        }
        return null;
    }

    private List<ViewObject> getQuestionsInfo(int localUserId, List<Integer> questionIds) {
        List<ViewObject> questionInfos = new ArrayList<>();
        for (int qid : questionIds) {
            Question question = questionService.selectById(qid);
            if (question == null) {
                continue;
            }

            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, qid));
            vo.set("commentCount", commentService.getCommentCount(qid, EntityType.ENTITY_QUESTION));

            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_QUESTION, qid));
            } else {
                vo.set("followed", false);
            }
            questionInfos.add(vo);
        }
        return questionInfos;
    }

    /**
     * 访问问题所有的关注者列表
     */
    @RequestMapping(path = {"/question/{qid}/followers"}, method = RequestMethod.GET)
    public String questionFollowers(@PathVariable("qid") int qid, Model model) {
        try {
            List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_QUESTION, qid, 10);
            if (hostHolder.getUser() != null) {
                model.addAttribute("userInfos", getUsersInfo(hostHolder.getUser().getId(), followerIds));
            } else {
                model.addAttribute("userInfos", getUsersInfo(0, followerIds));
            }
            model.addAttribute("curQuestionTitle", questionService.selectById(qid).getTitle());
            model.addAttribute("curFollowerCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, qid));
            return "questionFollower";
        } catch (Exception e) {
            logger.error("访问问题的关注者列表出错：" + e.getMessage());
        }
        return null;
    }

    @RequestMapping(path = {"/followUser"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String followUser(@RequestParam("userId") int uid) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999, "未登录");
        }

        boolean res = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, uid);

        return WendaUtil.getJSONString(res ? 0 : 1, String.valueOf(
                followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    @RequestMapping(path = {"/unfollowUser"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int uid) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999, "未登录");
        }

        boolean res = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, uid);

        return WendaUtil.getJSONString(res ? 0 : 1, String.valueOf(
                followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    @RequestMapping(path = {"/followQuestion"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int qid) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999, "未登录");
        }

        Question question = questionService.selectById(qid);
        if (question == null) {
            return WendaUtil.getJSONString(1, "问题不存在");
        }

        boolean res = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid);

        return WendaUtil.getJSONString(res ? 0 : 1, String.valueOf(
                followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION)));
    }

    @RequestMapping(path = {"/unfollowQuestion"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int qid) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999, "未登录");
        }

        Question question = questionService.selectById(qid);
        if (question == null) {
            return WendaUtil.getJSONString(1, "问题不存在");
        }

        boolean res = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid);

        return WendaUtil.getJSONString(res ? 0 : 1, String.valueOf(
                followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION)));
    }
}
