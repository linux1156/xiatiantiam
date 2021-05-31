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

    /**
     *
     * @param key
     * @param indexdb 选择的redis库 0-15
     * @return 成功返回value 失败返回null
     */
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

    /**
     *
     * @param key
     * @param indexdb 选择redis库0-15
     * @return 成功返回value 失败返回null
     */
    public byte[] get(byte[] key, int indexdb){
        Jedis jedis = null;
        byte[] value = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexdb);
            value = jedis.get(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return value;
    }

    /**
     *
     * @param key
     * @param value
     * @param indexdb 选择的redis库 0-15
     * @param ttl 传递的key的过期时间
     */
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

    /**
     *
     * @param key 存入的键
     * @param value 存储的值
     * @param indexdb 选择的redis库 0-15
     */
    public void set(byte[] key, byte[] value, int indexdb){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexdb);
            jedis.set(key, value);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
    }

    /**
     *
     * @param indexdb 选择的redis库 0-15
     * @param keys 一个key，也可以是string数组
     * @return 返回删除成功的个数
     */
    public Long del(int indexdb, String... keys){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexdb);
            return jedis.del(keys);
        }catch (Exception e){
            log.error(e.getMessage());
            return 0L;
        }finally {
            jedis.close();
        }
    }

    /**
     *
     * @param indexdb 选择的redis库 0-15
     * @param keys 一个key 也可以是string数组
     * @return 返回删除成功的个数
     */
    public Long del(int indexdb, byte[]... keys){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexdb);
            return jedis.del(keys);
        } catch (Exception e){
            log.error(e.getMessage());
            return 0L;
        }finally {
            jedis.close();
        }
    }

    /**
     *
     * @param key
     * @param str
     * @return 成功返回 添加后value的长度 失败 返回 添加的 value 的长度 异常返回0L
     */
    public Long append(String key, String str){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.append(key, str);
        }catch (Exception e){
            log.error(e.getMessage());
            return 0L;
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     *
     * @param key
     * @return true or false
     */
    public Boolean exists(String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.exists(key);
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }finally {
            jedis.close();
        }
    }

    /**
     * 清空当前数据库中的所有 key，此命令从不失败
     * @return 总是返回ok
     */
    public String flushDB(){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.flushDB();
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return null;
    }

    /**
     *
     * @param key
     * @param ttl 过期时间，单位:秒
     * @param indexdb redis选择的库 0-15
     * @return 成功返回1 如果存在 和 发生异常 返回 0
     */
    public Long expire(String key, int ttl, int indexdb){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexdb);
            return jedis.expire(key, ttl);
        }catch (Exception e){
            log.error(e.getMessage());
            return 0L;
        }finally {
            jedis.close();
        }
    }

    /**
     *
     * @param key
     * @param indexdb
     * @return 当key不存在的时候，返回-2 当key存在但没有设置剩余生存时间时，返回-1。否则以秒为单位，返回key的剩余生存时间。发生异常 返回 0
     */
    public Long ttl(String key, int indexdb){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexdb);
            return jedis.ttl(key);
        }catch (Exception e){
            log.error(e.getMessage());
            return 0L;
        }finally {
            jedis.close();
        }
    }

    /**
     * <p>
     *     移除给定key的生存时间，将这个key从【易失的】（带生存时间key）转换成【持久的】（一个不带生存时间、永不过期的key）
     * </p>
     * @param key
     * @return 当生存时间移除成功时，返回 1 如果key不存在或者key没有设置生存时间 返回 0，发生异常 返回-1
     */
    public Long persist(String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.persist(key);
        }catch (Exception e){
            log.error(e.getMessage());
            return -1L;
        }finally {
            jedis.close();
        }
    }

    /**
     * <p>
     *     新增key，并设置key的生存时间（以秒为单位）
     * </p>
     * @param key 键
     * @param seconds 生存时间 单位：秒
     * @param value
     * @return 设置成功返回ok。当seconds参数不合法时，返回错误信息
     */
    public String setex(String key, int seconds, String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.setex(key, seconds, value);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return null;
    }


    /**
     * <p>
     *  设置key value, 如果key已经存在则返回0 nx ==> not exist
     * </p>
     * @param key
     * @param value 成功返回1 如果存在 和 发生异常 返回 0
     * @return
     */
    public Long setnx(String key, String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.setnx(key, value);
        }catch (Exception e){
            log.error(e.getMessage());
            return 0L;
        }finally {
            jedis.close();
        }
    }

    /**
     * <p>
     *     将给定key的值设为value 并返回key的旧值(old value)
     *     当key存在但不是一个字符串类型时 返回一个错误
     * </p>
     * @param key
     * @param value
     * @return 返回给定key的旧值 当key没有旧值时 也即是 key不存在时 返回nil
     */
    public String getSet(String key, String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.getSet(key, value);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return null;
    }

    /**
     * <p>
     *     设置key value并制定这个键值的有效期
     * </p>
     * @param key
     * @param value
     * @param seconds 单位（秒）
     * @return 成功返回ok 失败返回null
     */
    public String setex(String key, String value, int seconds){
        Jedis jedis = null;
        String res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.setex(key, seconds, value);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }


}
