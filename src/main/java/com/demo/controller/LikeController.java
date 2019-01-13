package com.demo.controller;

import com.demo.async.EventModel;
import com.demo.async.EventProducer;
import com.demo.async.EventType;
import com.demo.model.Comment;
import com.demo.model.EntityType;
import com.demo.model.HostHolder;
import com.demo.service.CommentService;
import com.demo.service.LikeService;
import com.demo.util.JedisAdapter;
import com.demo.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    private Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = {"/like"}, method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        try {
            if (hostHolder.getUser() == null) {
                return WendaUtil.getJSONString(999, "未登录");
            }

            Comment comment = commentService.getCommentById(commentId);

            // 超长链式设置！
            eventProducer.fireEvent(new EventModel().setEventType(EventType.LIKE)
                    .setActorId(hostHolder.getUser().getId())
                    .setEntityType(EntityType.ENTITY_COMMENT)
                    .setEntityId(commentId)
                    .setEntityOwnerId(comment.getUserId())
                    .setExts("questionId", String.valueOf(comment.getEntityId())));

            likeService.like(hostHolder.getUser().getId(), commentId, EntityType.ENTITY_COMMENT);
            long likeCount = likeService.getLikeCount(commentId, EntityType.ENTITY_COMMENT);
            return WendaUtil.getJSONString(0, String.valueOf(likeCount));
        } catch (Exception e) {
            logger.error("赞失败：" + e.getMessage());
        }
        return WendaUtil.getJSONString(1, "赞失败");
    }

    @RequestMapping(path = {"/dislike"}, method = RequestMethod.POST)
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        try {
            if (hostHolder.getUser() == null) {
                return WendaUtil.getJSONString(999, "未登录");
            }

//            Comment comment = commentService.getCommentById(commentId, EntityType.ENTITY_COMMENT);
            likeService.dislike(hostHolder.getUser().getId(), commentId, EntityType.ENTITY_COMMENT);
            long dislikeCount = likeService.getDislikeCount(commentId, EntityType.ENTITY_COMMENT);
            return WendaUtil.getJSONString(0, String.valueOf(dislikeCount));
        } catch (Exception e) {
            logger.error("踩失败：" + e.getMessage());
        }
        return WendaUtil.getJSONString(1, "踩失败");
    }
}
