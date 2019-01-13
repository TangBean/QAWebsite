package com.demo.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.util.JedisAdapter;
import com.demo.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    private Logger logger = LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    private JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel model) {
        try {
            String key = RedisKeyUtil.getEventQueueKey();
//            String jsonString = JSON.toJSONString(model);
            String jsonString = JSONObject.toJSONString(model);
            jedisAdapter.lpush(key, jsonString);
            return true;
        } catch (Exception e) {
            logger.error("添加异步任务失败" + e.getMessage());
            return false;
        }
    }
}
