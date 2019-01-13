package com.demo.service;

import com.demo.util.JedisAdapter;
import com.demo.util.RedisKeyUtil;
import com.demo.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowService {
    private final Logger logger = LoggerFactory.getLogger(FollowService.class);

    @Autowired
    private JedisAdapter jedisAdapter;

    public boolean follow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Date date = new Date();

        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zadd(followeeKey, date.getTime(), String.valueOf(entityId));
        tx.zadd(followerKey, date.getTime(), String.valueOf(userId));
        List<Object> res = jedisAdapter.exec(jedis, tx);
        return res.size() == 2 && (long) res.get(0) > 0 && (long) res.get(1) > 0;
    }

    public boolean unfollow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Date date = new Date();

        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zrem(followeeKey, String.valueOf(entityId));
        tx.zrem(followerKey, String.valueOf(userId));
        List<Object> res = jedisAdapter.exec(jedis, tx);
        return res.size() == 2 && (long) res.get(0) > 0 && (long) res.get(1) > 0;
    }

    public List<Integer> getFollowers(int entityType, int entityId, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey, 0, count));
    }

    public List<Integer> getFollowees(int userId, int entityType, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, 0, count));
    }

    public List<Integer> getFollowers(int entityType, int entityId, int offset, int limit) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey, offset, offset + limit));
    }

    public List<Integer> getFollowees(int userId, int entityType, int offset, int limit) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, offset, offset + limit));
    }

    private List<Integer> getIdsFromSet(Set<String> set) {
        List<Integer> res = new ArrayList<>();
        for (String s : set) {
            res.add(Integer.parseInt(s));
        }
        return res;
    }

    public int getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return (int) jedisAdapter.zcard(followerKey);
    }

    public int getFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return (int) jedisAdapter.zcard(followeeKey);
    }

    public boolean isFollower(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        // 通过是否能查找到userId对应的分数来判断这个用户是否在key中
        return jedisAdapter.zscore(followerKey, String.valueOf(userId)) != null;
    }

}
