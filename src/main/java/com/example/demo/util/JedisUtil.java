package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
@Slf4j
public class JedisUtil {

    @Autowired
    private JedisPool jedisPool;

    public String get(String key, int indexdb){
        Jedis jedis = null;
        String value = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexdb);
            value = jedis.get(key);
        } catch (Exception e){
            return "";
        }finally {
            jedis.close();
        }
        return value;
    }

    public void set(String key, String value,int indexdb, int ttl){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexdb);
            jedis.set(key, value);
            jedis.expire(key, ttl);
        } catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
    }

}
