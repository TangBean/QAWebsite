package com.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.demo.model.User;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.concurrent.TimeUnit;

public class JedisDemo {
    public static void main(String[] args) throws InterruptedException {
        JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);
        Jedis jedis = jedisPool.getResource();
        // 删除Redis中的所有key
        jedis.flushDB();

        /* set操作 */
        jedis.set("aaaa", "bbbb");
        jedis.setex("bbbb", 1, "存在1秒");
        System.out.println(jedis.get("bbbb")); // Output：存在15秒
        Thread.sleep(1000);
        System.out.println(jedis.get("bbbb")); // Output：null

        /* rename操作 */
        System.out.println(jedis.get("aaaa")); // Output：bbbb
        jedis.rename("aaaa", "cccc");
        System.out.println(jedis.get("aaaa")); // Output：null
        System.out.println(jedis.get("cccc")); // Output：bbbb

        /* 加减法操作 */
        jedis.set("pv", "100");
        System.out.println(jedis.incr("pv")); // Output：101
        System.out.println(jedis.incrBy("pv", 5)); // Output：106
        System.out.println(jedis.decr("pv")); // Output：105
        System.out.println(jedis.decrBy("pv", 5)); // Output：100

        /* 正则表达式匹配keys */
        System.out.println(jedis.keys("*")); // jedis.keys(String)的返回值是Set<String>

        /* List：双向链表，适用于最新列表，关注列表，可以拿来当栈、队列用 */
        String listName = "list";
        jedis.del(listName); // 如果Redis中已经存在该key，把它删除

        // 栈的push，pop操作
        for (int i = 0; i < 5; i++) {
            jedis.lpush(listName, "a" + String.valueOf(i + 1)); // 左push
            jedis.rpush(listName, "a" + String.valueOf(i + 1)); // 右push
        }
        System.out.println(jedis.lrange(listName, 0, jedis.llen(listName))); // lrange()返回List<String>
        jedis.lpop(listName); // 左pop
        jedis.rpop(listName); // 右pop
        System.out.println(jedis.lrange(listName, 0, jedis.llen(listName))); // lrange()返回List<String>

        // 按引索取元素
        System.out.println(jedis.lindex(listName, 2));

        // 删除前2个value=a1的元素
        jedis.lpush(listName, "a1");
        jedis.lrem(listName, 2, "a1");
        System.out.println(jedis.lrange(listName, 0, jedis.llen(listName)));

        // 在指定位置插入元素
        jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a1", "a1前");
        jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a1", "a1后");
        System.out.println(jedis.lrange(listName, 0, jedis.llen(listName)));

        // 改变指定位置的值
        jedis.lset(listName, 0, "ahead");
        System.out.println(jedis.lrange(listName, 0, jedis.llen(listName)));


        /* Hash：对象属性，适用于不定长属性数 */
        String hashName = "hash";
        jedis.del(hashName);

        // get, set, getAll, del操作
        jedis.hset(hashName, "name", "Tom");
        jedis.hset(hashName, "age", "3");
        System.out.println(jedis.hget(hashName, "name"));
        System.out.println(jedis.hget(hashName, "age"));
        System.out.println(jedis.hgetAll(hashName));
        jedis.hdel(hashName, "age");
        System.out.println(jedis.hexists(hashName, "age"));
        System.out.println(jedis.hkeys(hashName));
        System.out.println(jedis.hvals(hashName));

        // nx：not exists，不存在才set，存在就不set了
        jedis.hsetnx(hashName, "age", "4");
        jedis.hsetnx(hashName, "age", "3");
        System.out.println(jedis.hget(hashName, "age")); // Output：4


        /* Set：适用于无顺序的集合，点赞点踩，抽奖，已读，共同好友，用来去重等 */
        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";
        jedis.del(likeKey1);
        jedis.del(likeKey2);

        // 添加元素
        for (int i = 0; i < 10; ++i) {
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i*i));
        }
        System.out.println(jedis.smembers(likeKey1));
        System.out.println(jedis.smembers(likeKey2));

        // 从集合删除元素
        System.out.println(jedis.sismember(likeKey1, "4"));
        jedis.srem(likeKey1, "4");
        System.out.println(jedis.sismember(likeKey1, "4"));

        // 在集合之间移动元素
        System.out.println(jedis.sismember(likeKey2, "4"));
        jedis.smove(likeKey2, likeKey1, "4");
        System.out.println(jedis.sismember(likeKey1, "4"));
        System.out.println(jedis.sismember(likeKey2, "4"));

        // 集合的size
        System.out.println(jedis.scard(likeKey1));
        System.out.println(jedis.scard(likeKey2));

        // 集合运算
        System.out.println(jedis.sinter(likeKey1, likeKey2)); // 交集
        System.out.println(jedis.sunion(likeKey1, likeKey2)); // 并集
        System.out.println(jedis.sdiff(likeKey1, likeKey2));  // likeKey1 - 交集
        System.out.println(jedis.sdiff(likeKey2, likeKey1));  // likeKey2 - 交集

        // 随机取n个，可以用来做抽奖
        System.out.println(jedis.srandmember(likeKey2, 5));


        /* SortedSet：适用于排行榜，优先队列 */
        String rankName = "rankKey";
        jedis.del(rankName);

        // 添加 (优先级 - 名称)
        jedis.zadd(rankName, 15, "jim");
        jedis.zadd(rankName, 60, "Ben");
        jedis.zadd(rankName, 90, "Lee");
        jedis.zadd(rankName, 75, "Lucy");
        jedis.zadd(rankName, 80, "Mei");
        System.out.println(jedis.zcard(rankName));
        System.out.println(jedis.zcount(rankName, 60, 90)); // 统计在60~90之间的个数，包括60和90

        // 一个元素的优先级
        System.out.println(jedis.zscore(rankName, "Lucy"));

        // 增加一个元素的优先级
        System.out.println(jedis.zincrby(rankName, 2, "Lucy"));

        // 正向排序和反向排序
        System.out.println(jedis.zrange(rankName, 0, 4));
        System.out.println(jedis.zrange(rankName, 0, 2));
        System.out.println(jedis.zrevrange(rankName, 0, 4));

        // 遍历
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankName, "60", "100")) {
            System.out.println(tuple.getElement() + ": " + String.valueOf(tuple.getScore()));
        }

        // 得到Ben的正向排名和反向排名
        System.out.println(jedis.zrank(rankName, "Ben"));
        System.out.println(jedis.zrevrank(rankName, "Ben"));

        String setKey = "zset";
        jedis.zadd(setKey, 1, "a");
        jedis.zadd(setKey, 1, "b");
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "d");
        jedis.zadd(setKey, 1, "e");

        // 得到优先级在一定范围的数的个数
        System.out.println(jedis.zlexcount(setKey, "-", "+"));   // 在(-INF, +INF)范围内的个数
        System.out.println(jedis.zlexcount(setKey, "[b", "[d")); // 在(b, d]范围内的个数
        System.out.println(jedis.zlexcount(setKey, "(b", "[d")); // 在[b, d]范围内的个数

        /* 通过JSON序列化对象，并用Redis缓存 */
        User user1 = new User();
        user1.setId(1);
        user1.setName("Bean");
        user1.setPassword("1234");
        user1.setSalt("salt1234");
        System.out.println(JSONObject.toJSONString(user1));
        jedis.set("user1", JSONObject.toJSONString(user1));
        User user2 = JSONObject.parseObject(jedis.get("user1"), User.class);
        System.out.println("id: " + user2.getId());
        System.out.println("name: " + user2.getName());
        System.out.println("password: " + user2.getPassword());
        System.out.println("salt: " + user2.getSalt());

        jedis.close(); // 一定要记得close！连接池有8条线程，不还后面的就不能继续
    }
}
