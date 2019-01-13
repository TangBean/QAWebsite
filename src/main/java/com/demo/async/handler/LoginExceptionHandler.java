package com.demo.async.handler;

import com.demo.async.EventHandler;
import com.demo.async.EventModel;
import com.demo.async.EventType;
import com.demo.util.MailSender;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    private MailSender mailSender;

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }

    @Override
    public void doHandler(EventModel model) {
        String to = model.getExts("email");
        String subject = model.getExts("subject");
        String templateName = model.getExts("templateName");
        Map<String, Object> map = new HashMap<>();
        map.put("username", model.getExts("username"));
        map.put("loginTime", new Date());
        mailSender.sendWithHTMLTemplate(to, subject, templateName, map);
    }
}
