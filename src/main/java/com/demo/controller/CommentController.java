package com.demo.controller;

import com.demo.async.EventModel;
import com.demo.async.EventProducer;
import com.demo.async.EventType;
import com.demo.model.Comment;
import com.demo.model.EntityType;
import com.demo.model.Feed;
import com.demo.model.HostHolder;
import com.demo.service.CommentService;
import com.demo.service.FeedService;
import com.demo.service.QuestionService;
import com.demo.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.parser.Entity;
import java.util.Date;

@Controller
public class CommentController {
    private Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private FeedService feedService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = {"/comment/add"}, method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            if (hostHolder.getUser() != null) {
                comment.setUserId(hostHolder.getUser().getId());
            } else {
                comment.setUserId(WendaUtil.ANONYMOUS_USERID);
            }
            comment.setContent(content);
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setCreatedDate(new Date());

            commentService.addComment(comment);

            int commentCount = commentService.getCommentCount(questionId, EntityType.ENTITY_QUESTION);
            questionService.updateCommentCount(questionId, commentCount);

            eventProducer.fireEvent(new EventModel().setActorId(comment.getUserId())
                    .setEventType(EventType.COMMENT).setEntityType(EntityType.ENTITY_QUESTION)
                    .setEntityId(questionId));

        } catch (Exception e) {
            logger.error("添加问题的评论失败：" + e.getMessage());
        }

        return "redirect:/question/" + questionId;
    }
}
