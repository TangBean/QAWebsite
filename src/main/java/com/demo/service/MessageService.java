package com.demo.service;

import com.demo.dao.MessageDao;
import com.demo.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageService {
    private Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private SensitiveService sensitiveService;

    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDao.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDao.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int localUserId, int offset, int limit) {
        return messageDao.getConversationList(localUserId, offset, limit);
    }

    public int getConversationUnreadCount(int userId, String conversationId) {
        return messageDao.getConversationUnreadCount(userId, conversationId);
    }

    public void updateUnreadStatus(String conversationId) {
        messageDao.updateUnreadStatus(conversationId);
    }
}
