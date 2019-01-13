package com.demo.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.demo.async.EventHandler;
import com.demo.async.EventModel;
import com.demo.async.EventType;
import com.demo.model.EntityType;
import com.demo.model.Feed;
import com.demo.model.Question;
import com.demo.model.User;
import com.demo.service.FeedService;
import com.demo.service.FollowService;
import com.demo.service.QuestionService;
import com.demo.service.UserService;
import com.demo.util.JedisAdapter;
import com.demo.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FeedHandler implements EventHandler {
    private static final long PUSH_FEED_LEN_LIMIT = 20;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private FeedService feedService;

    @Autowired
    private FollowService followService;

    @Autowired
    private JedisAdapter jedisAdapter;

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }

    @Override
    public void doHandler(EventModel model) {
        Feed feed = new Feed();
        feed.setType(model.getEventType().getValue());
        feed.setUserId(model.getActorId());
        feed.setCreateDate(new Date());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null) {
            return;
        }
        feedService.addFeed(feed);

        // push方式推送新鲜事
        List<Integer> followerList = followService.getFollowers(
                EntityType.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);
        followerList.add(0); // 推给匿名用户，接收所有feed
        for (int userId : followerList) {
            String key = RedisKeyUtil.getFeedKey(userId);
            jedisAdapter.lpush(key, String.valueOf(feed.getId()));
            jedisAdapter.ltrim(key, 0L, PUSH_FEED_LEN_LIMIT - 1);
        }

    }

    private String buildFeedData(EventModel model) {
        Map<String, String> map = new HashMap<>();
        User actorUser = userService.getById(model.getActorId());
        if (actorUser == null) {
            return null;
        }

        map.put("userId", String.valueOf(actorUser.getId()));
        map.put("userHead", String.valueOf(actorUser.getHeadUrl()));
        map.put("userName", String.valueOf(actorUser.getName()));

        if (model.getEventType() == EventType.COMMENT
                || (model.getEventType() == EventType.FOLLOW
                    && model.getEntityType() == EntityType.ENTITY_QUESTION)) {
            Question question = questionService.selectById(model.getEntityId());
            if (question == null) {
                return null;
            }

            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }
}
