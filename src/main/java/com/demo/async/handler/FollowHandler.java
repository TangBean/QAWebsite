package com.demo.async.handler;

import com.demo.async.EventHandler;
import com.demo.async.EventModel;
import com.demo.async.EventType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class FollowHandler implements EventHandler {
    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.FOLLOW});
    }

    @Override
    public void doHandler(EventModel model) {

    }
}
