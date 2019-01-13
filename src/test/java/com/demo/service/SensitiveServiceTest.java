package com.demo.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SensitiveServiceTest {
    private Logger logger = LoggerFactory.getLogger(SensitiveServiceTest.class);

    @Autowired
    SensitiveService sensitiveService;

    @Test
    public void afterPropertiesSet() {
        sensitiveService.afterPropertiesSet();
//        logger.info("finish");
    }

    @Test
    public void filter() {
        sensitiveService.afterPropertiesSet();
        System.out.println(sensitiveService.filter("你好，赌 博，这是~ 色 ~ 情 ~"));
    }
}