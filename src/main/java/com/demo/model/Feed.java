package com.demo.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class Feed {
    private int id;

    private int type;

    private int userId;

    private Date createDate;

    private String data;

    private JSONObject dataJSON;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        this.dataJSON = JSONObject.parseObject(data);
    }

    public String get(String key) {
        return dataJSON.getString(key);
    }
}
