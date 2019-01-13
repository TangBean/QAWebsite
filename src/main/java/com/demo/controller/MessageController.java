package com.demo.controller;

import com.demo.model.HostHolder;
import com.demo.model.Message;
import com.demo.model.User;
import com.demo.model.ViewObject;
import com.demo.service.MessageService;
import com.demo.service.UserService;
import com.demo.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = {"/msg/list"}, method = RequestMethod.GET)
    public String getConversationList(Model model) {
        if (hostHolder.getUser() == null) {
            return "redirect:/reglogin?next=/msg/list";
        }
        List<Message> messageList = messageService.getConversationList(hostHolder.getUser().getId(), 0, 10);
        List<ViewObject> conversationList = new ArrayList<>();
        for (Message message : messageList) {
            ViewObject vo = new ViewObject();
            vo.set("message", message);
            if (message.getFromId() != hostHolder.getUser().getId()) {
                vo.set("targetUser", userService.getById(message.getFromId()));
            } else {
                vo.set("targetUser", userService.getById(message.getToId()));
            }
            int unreadCount = messageService.getConversationUnreadCount(
                    hostHolder.getUser().getId(), message.getConversationId());
            vo.set("unreadCount", unreadCount);
            conversationList.add(vo);
        }
        model.addAttribute("conversationList", conversationList);
        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"}, method = RequestMethod.GET)
    public String getConversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
        if (hostHolder.getUser() == null) {
            return "redirect:/reglogin?next=/msg/detail?conversationId=" + conversationId;
        }
        messageService.updateUnreadStatus(conversationId);
        List<Message> messageList = messageService.getConversationDetail(conversationId, 0, 10);
        List<ViewObject> conversationList = getConversationList(messageList);
        model.addAttribute("conversationList", conversationList);
        return "letterDetail";
    }

    private List<ViewObject> getConversationList(List<Message> messageList) {
        List<ViewObject> conversationList = new ArrayList<>();
        for (Message message : messageList) {
            User fromUser = userService.getById(message.getFromId());
            ViewObject vo = new ViewObject();
            vo.set("fromUser", fromUser);
            vo.set("message", message);
            conversationList.add(vo);
        }
        return conversationList;
    }

    @RequestMapping(path = {"/msg/addMessage"}, method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("") String toName,
                             @RequestParam("content") String content) {
        try {
            Message message = new Message();
            if (hostHolder.getUser() == null) {
                message.setFromId(WendaUtil.ANONYMOUS_USERID);
            } else {
                message.setFromId(hostHolder.getUser().getId());
            }
            User toUser = userService.getBName(toName);
            message.setToId(toUser.getId());
            message.setContent(content);
            message.setConversationId();
            message.setCreatedDate(new Date());
            message.setIsRead(0);
            if (messageService.addMessage(message) > 0) {
                return WendaUtil.getJSONString(0, "发送私信成功");
            }
            return WendaUtil.getJSONString(1, "发送私信失败");
        } catch (Exception e) {
            logger.error("发送私信失败" + e.getMessage());
            return WendaUtil.getJSONString(1, "发送私信失败");
        }
    }




}
