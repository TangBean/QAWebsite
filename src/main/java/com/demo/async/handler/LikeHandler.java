package com.demo.async.handler;

import com.demo.async.EventHandler;
import com.demo.async.EventModel;
import com.demo.async.EventType;
import com.demo.model.Message;
import com.demo.model.User;
import com.demo.service.MessageService;
import com.demo.service.UserService;
import com.demo.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.LIKE});
    }

    @Override
    public void doHandler(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        User user = userService.getById(model.getActorId());
        message.setContent("用户" + user.getName() + "赞了你的评论！http://localhost:8080/question/"
                + model.getExts("questionId"));
        message.setConversationId();
        message.setCreatedDate(new Date());
        message.setIsRead(0);
        messageService.addMessage(message);
    }
}
