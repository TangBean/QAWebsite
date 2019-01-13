package com.demo.dao;

import com.demo.model.Question;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionDaoTest {

    @Autowired
    private QuestionDao questionDao;

    @Test
    public void selectByUserIdAndOffset() {
        List<Question> questionList = questionDao.selectByUserIdAndOffset(0, 0, 10);
        for (Question question : questionList) {
            System.out.println(question);
        }
    }
}