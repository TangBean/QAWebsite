package com.demo.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailSenderTest {
    private Logger logger = LoggerFactory.getLogger(MailSenderTest.class);

    @Autowired
    private MailSender mailSender;

    @Test
    public void sendWithHTMLTemplate() {
        String to = "984354469@qq.com";
        String subject = "登录提醒";
        String templateName = "mail/loginMailTemp.ftl";
        Map<String, Object> model = new HashMap<>();
        model.put("username", to);
        model.put("loginTime", new Date());
        mailSender.sendWithHTMLTemplate(to, subject, templateName, model);
        logger.info("邮件发送结束");
    }
}