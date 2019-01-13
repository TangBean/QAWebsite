package com.demo.model;

import java.util.Date;

public class Message {

    private int id;

    private int fromId;

    private int toId;

    private String content;

    private String conversationId;

    private Date createdDate;

    // 0表示未读，1表示已读
    private int isRead;

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId() {
        if (fromId < toId) {
            conversationId = String.valueOf(fromId) + "_" + String.valueOf(toId);
        } else {
            conversationId = String.valueOf(toId) + "_" + String.valueOf(fromId);
        }
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
