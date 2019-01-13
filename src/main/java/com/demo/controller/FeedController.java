package com.demo.controller;

import com.demo.model.EntityType;
import com.demo.model.Feed;
import com.demo.model.HostHolder;
import com.demo.service.FeedService;
import com.demo.service.FollowService;
import com.demo.util.JedisAdapter;
import com.demo.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FeedController {
    private Logger logger = LoggerFactory.getLogger(FeedController.class);

    @Autowired
    private FeedService feedService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private FollowService followService;

    @Autowired
    private JedisAdapter jedisAdapter;

    @RequestMapping(path = {"/pullfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String getPullFeeds(Model model) {
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<Integer> followeeList = new ArrayList<>();
        if (localUserId != 0) {
            followeeList = followService.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
        }
        List<Feed> feedList = feedService.getUserFeeds(Integer.MAX_VALUE, followeeList, 10);
        model.addAttribute("feedList", feedList);
        return "feeds";
    }

    @RequestMapping(path = {"/pushfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String getPushFeeds(Model model) {
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        String key = RedisKeyUtil.getFeedKey(localUserId);
        List<String> feedIds = jedisAdapter.lrange(key, 0, 10);

        List<Feed> feedList = new ArrayList<>();
        for (String feedId : feedIds) {
            Feed feed = feedService.getById(Integer.valueOf(feedId));
            if (feed != null) {
                feedList.add(feed);
            }
        }
        model.addAttribute("feedList", feedList);
        return "feeds";
    }
}
