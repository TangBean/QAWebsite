package com.demo.service;

import com.demo.dao.CommentDao;
import com.demo.model.Comment;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {
    private Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private SensitiveService sensitiveService;

    public Comment getCommentById(int commentId) {
        return commentDao.selectCommentById(commentId);
    }

    public int addComment(Comment comment) {
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDao.addComment(comment);
    }

    public List<Comment> selectCommentsByEntity(int entityId, int entityType) {
        return commentDao.selectCommentsByEntity(entityId, entityType);
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDao.getCommentCount(entityId, entityType);
    }

    public boolean deleteComment(int commentId, int status) {
        return commentDao.updateCommentStatus(commentId, status) > 0;
    }

    public int getUserCommentCount(int userId) {
        return commentDao.getUserCommentCount(userId);
    }
}
