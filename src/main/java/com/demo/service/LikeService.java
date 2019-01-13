package com.demo.service;

import com.demo.util.JedisAdapter;
import com.demo.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    private Logger logger = LoggerFactory.getLogger(LikeService.class);

    @Autowired
    private JedisAdapter jedisAdapter;

    public long like(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));

        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        jedisAdapter.srem(dislikeKey, String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }

    public long dislike(int userId, int entityType, int entityId) {
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        jedisAdapter.sadd(dislikeKey, String.valueOf(userId));

        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey, String.valueOf(userId));

        return jedisAdapter.scard(dislikeKey);
    }

    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        }

        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        if (jedisAdapter.sismember(dislikeKey, String.valueOf(userId))) {
            return -1;
        }

        return 0;
    }

    public int getLikeCount(int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return (int) jedisAdapter.scard(likeKey);
    }

    public int getDislikeCount(int entityType, int entityId) {
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        return (int) jedisAdapter.scard(dislikeKey);
    }
}
