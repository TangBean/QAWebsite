package com.demo.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.util.JedisAdapter;
import com.demo.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventConsumer implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    private Map<EventType, List<EventHandler>> config = new HashMap<>();

    private ApplicationContext applicationContext;

    @Autowired
    private JedisAdapter jedisAdapter;

    public EventConsumer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);

        if (beans != null) {
            for (EventHandler handler : beans.values()) {
                List<EventType> eventTypes = handler.getSupportEventTypes();

                for (EventType eventType : eventTypes) {
                    if (!config.containsKey(eventType)) {
                        config.put(eventType, new ArrayList<>());
                    }
                    config.get(eventType).add(handler);
                }
            }
        }

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> event = jedisAdapter.brpop(0, key);

                    EventModel eventModel = JSON.parseObject(event.get(1), EventModel.class);
                    if (!config.containsKey(eventModel.getEventType())) {
                        logger.error("不能识别该事件");
                        continue;
                    }
                    for (EventHandler handler : config.get(eventModel.getEventType())) {
                        handler.doHandler(eventModel);
                    }
                }
            }
        }.start();
    }
}
