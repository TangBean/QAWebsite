package com.demo.async.handler;

import com.demo.async.EventHandler;
import com.demo.async.EventModel;
import com.demo.async.EventType;
import com.demo.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AddQuestionHandler implements EventHandler {

    @Autowired
    private SearchService searchService;

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[] {EventType.QUESTION});
    }

    @Override
    public void doHandler(EventModel model) {
        int qid = Integer.parseInt(model.getExts("qid"));
        String title = model.getExts("title");
        String content = model.getExts("content");
        searchService.indexQuery(qid, title, content);
    }
}
