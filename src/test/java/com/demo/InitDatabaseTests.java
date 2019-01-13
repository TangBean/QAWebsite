package com.demo;


import com.demo.dao.QuestionDao;
import com.demo.dao.UserDao;
import com.demo.model.EntityType;
import com.demo.model.Question;
import com.demo.model.User;
import com.demo.service.FollowService;
import com.demo.service.SensitiveService;
import com.demo.util.JedisAdapter;
import com.demo.util.WendaUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitDatabaseTests {
    @Autowired
    UserDao userDao;

    @Autowired
    QuestionDao questionDao;

    @Autowired
    SensitiveService sensitiveUtil;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Test
    public void contextLoads() {
        Random random = new Random();
        jedisAdapter.getJedis().flushDB();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("FOLLOW_TEST_USER%d", i+1));
            user.setPassword("");
            user.setSalt("");
            userDao.addUser(user);

            for (int j = 1; j < i; ++j) {
                followService.follow(j, EntityType.ENTITY_USER, i);
            }

            user.setPassword(WendaUtil.MD5("newpassword"));
            userDao.updatePassword(user);

            Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            question.setCreatedDate(date);
            question.setUserId(i + 1);
            question.setTitle(String.format("FOLLOW_TEST_TITLE{%d}", i));
            question.setContent(String.format("Balaababalalalal Content %d", i));
            questionDao.addQuestion(question);
        }

        System.out.println("finish");
//        Assert.assertEquals("newpassword", userDao.searchById(16).getPassword());
        //userDAO.deleteById(1);
        //Assert.assertNull(userDAO.selectById(1));
    }
}
