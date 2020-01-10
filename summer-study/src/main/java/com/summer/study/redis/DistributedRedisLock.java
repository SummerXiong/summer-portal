package com.summer.study.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Collections;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 * @date: 2020/1/9 16:51
 */
@Slf4j
public class DistributedRedisLock {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    private static final String host = "127.0.0.1";
    private static final int port = 6379;
    private static final String password = "";
    private static final int database = 0;
    private static final int maxIdle = 8;
    private static final long maxWaitMillis = 0;
    private static final int maxTotal = 20;
    private static final long minEvictableIdleTimeMillis = 300000;
    private static final long timeBetweenEvictionRunsMillis = 300000;


    public static JedisPool jedisPool(){

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(false);
        jedisPoolConfig.setTestWhileIdle(true);

        if(StringUtils.isNotBlank(password)){
            return new JedisPool(jedisPoolConfig, host, port, 10000, password, database);
        }else {
            return new JedisPool(jedisPoolConfig, host, port, 10000, null, database);
        }
    }

    /**
     * 尝试获取分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超时时间
     * @return 是否获取锁成功
     *
     * 第一个为key，我们使用key来当锁，因为key是唯一的。
     * 第二个为value，我们传的是requestId，很多童鞋可能不明白，有key作为锁不就够了吗，为什么还要用到value？
     *              原因就是我们在上面讲到可靠性时，分布式锁要满足第四个条件解铃还须系铃人，通过给
     *              value赋值为requestId，我们就知道这把锁是哪个请求加的了，在解锁的时候就可以有依据。
     *              requestId可以使用UUID.randomUUID().toString()方法生成。
     * 第三个为nxxx，这个参数我们填的是NX，意思是SET IF NOT EXIST，即当key不存在时，我们进行set操作；若key已经存在，则不做任何操作；
     * 第四个为expx，这个参数我们传的是PX，意思是我们要给这个key加一个过期的设置，具体时间由第五个参数决定。
     * 第五个为time，与第四个参数相呼应，代表key的过期时间。
     *
     */
    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime){
        Jedis jedis = null;
        try {
            // NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
            jedis = jedisPool().getResource();
            String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
            return LOCK_SUCCESS.equals(result) ? true : false;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            if(null != jedis){
                jedis.close();
            }
        }
    }

    /**
     * 释放分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseDistributedLock(String lockKey, String requestId) {

        Jedis jedis = null;
        try {
            jedis = jedisPool().getResource();
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
            return RELEASE_SUCCESS.equals(result) ? true : false;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            if(null != jedis){
                jedis.close();
            }
        }

    }
}
