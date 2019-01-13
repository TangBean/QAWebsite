package com.demo.service;

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
public class SearchServiceTest {

    @Autowired
    private SearchService searchService;

    @Test
    public void search() {
        List<Question> questionList = searchService.search("地球", 0, 10, "<em>", "</em>");
        for (Question q : questionList) {
            System.out.println(q);
        }
    }
}