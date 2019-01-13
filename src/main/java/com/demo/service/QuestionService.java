package com.demo.service;

import com.demo.dao.QuestionDao;
import com.demo.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {
    private Logger logger = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private SensitiveService sensitiveService;

    public int updateCommentCount(int questionId, int count) {
        return questionDao.updateCommentCount(questionId, count);
    }

    public List<Question> selectByUserIdAndOffset(int userId, int offset, int limit) {
        return questionDao.selectByUserIdAndOffset(userId, offset, limit);
    }

    public int addQuestion(Question question) {
        // escape the html label
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        // replace the sensitive words
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        return questionDao.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public List<Question> selectLatestQuestions(int offset, int limit) {
        return questionDao.selectLatestQuestions(offset, limit);
    }

    public Question selectById(int id) {
        return questionDao.selectById(id);
    }
}
