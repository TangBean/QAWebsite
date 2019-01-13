package com.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class JedisAdapter implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool;

    @Autowired
    private Environment env;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool(env.getProperty("spring.redis.host"));
    }


    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("JedisAdapter sadd wrong: " + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return -1;
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("JedisAdapter srem wrong: " + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return -1;
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("JedisAdapter scard wrong: " + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return -1;
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("JedisAdapter sismember wrong: " + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("JedisAdapter brpop wrong: " + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("JedisAdapter lpush wrong: " + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return -1;
    }

    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            logger.error("JedisAdapter lrange wrong: " + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public void ltrim(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.ltrim(key, start, end);
        } catch (Exception e) {
            logger.error("JedisAdapter ltrim wrong: " + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long zadd(String key, double score, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key, score, value);
        } catch (Exception e) {
            logger.error("JedisAdapter zadd wrong: " + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return -1;
    }

    public Set<String> zrevrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("JedisAdapter zrevrange wrong: " + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long zcount(String key, String min, String max) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcount(key, min, max);
        } catch (Exception e) {
            logger.error("JedisAdapter zcount wrong: " + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return -1;
    }

    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("JedisAdapter zcount wrong: " + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return -1;
    }

    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        } catch (Exception e) {
            logger.error("JedisAdapter zcount wrong: " + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0.0;
    }

    public Jedis getJedis() {
        return pool.getResource();
    }

    public Transaction multi(Jedis jedis) {
        try {
            return jedis.multi();
        } catch (Exception e) {
            logger.error("JedisAdapter multi wrong: " + e.getMessage());
        }
        return null;
    }

    public List<Object> exec(Jedis jedis, Transaction tx) {
        try {
            return tx.exec();
        } catch (Exception e) {
            logger.error("JedisAdapter exec wrong: " + e.getMessage());
            tx.discard();
        } finally {
            if (tx != null) {
                try {
                    tx.close();
                } catch (IOException e) {
                    logger.error("JedisAdapter tx.close() wrong: " + e.getMessage());
                }
            }
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
}
