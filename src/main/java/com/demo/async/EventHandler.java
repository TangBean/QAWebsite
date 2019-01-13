package com.demo.async;

import java.util.List;

public interface EventHandler {
    /**
     * 该事件处理器关心哪些事件类型
     * @return
     */
    List<EventType> getSupportEventTypes();

    /**
     * 该事件处理器应该如何处理给定的事件model
     * @param model
     */
    void doHandler(EventModel model);
}
