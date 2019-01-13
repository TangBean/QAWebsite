package com.demo.service;

import com.demo.dao.FeedDao;
import com.demo.model.Feed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedService {
    private Logger logger = LoggerFactory.getLogger(FeedService.class);

    @Autowired
    private FeedDao feedDao;

    public boolean addFeed(Feed feed) {
        return feedDao.addFeed(feed) > 0;
    }

    public Feed getById(int id) {
        return feedDao.getById(id);
    }

    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count) {
        return feedDao.selectUserFeeds(maxId, userIds, count);
    }
}
