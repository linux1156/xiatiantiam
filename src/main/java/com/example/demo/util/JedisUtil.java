package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ListPosition;
import redis.clients.jedis.SortingParams;

import java.util.List;
import java.util.Map;
import java.util.Set;

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


    /**
     *
     * @param key 键盘
     * @param str 替换的字符串
     * @param offset 下标位置
     * @return 返回替换后value长度
     */
    public Long setrange(String key, String str, int offset){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.setrange(key, offset, str);
        }catch (Exception e){
            log.error(e.getMessage());
            return 0L;
        }finally {
            jedis.close();
        }
    }

    /**
     * <p>
     *     批量批量的key获取批量的value
     * </p>
     * @param keys string数组 也可以是一个key
     * @return 成功返回value的集合 失败返回null的集合 异常返回空
     */
    public List<String> mget(String... keys){
        Jedis jedis = null;
        List<String> values = null;
        try{
            jedis = jedisPool.getResource();
            values = jedis.mget(keys);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return values;
    }

    /**
     * <p>
     *     批量的设置key:value 可以一个
     * </p>
     * <p>
     *     example:
     * </p>
     * <p>
     *     obj.mset(new String[]{"key1","value1","key2","value2"})
     * </p>
     * @param keysvalues
     * @return 成功返回ok 失败 异常 返回 null
     */
    public String mset(String... keysvalues){
        Jedis jedis = null;
        String res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.mset(keysvalues);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     批量的设置key:value 可以一个 如果key已经存在则失败 操作回滚
     * </p>
     * <p>
     *     obj.msetnx(new String[] {"key1", "value1", "key2", "value2"})
     * </p>
     * @param keysvalues
     * @return 成功返回1 失败返回0
     */
    public Long msetnx(String... keysvalues){
        Jedis jedis = null;
        Long res = 0L;
        try{
            jedis = jedisPool.getResource();
            res = jedis.msetnx(keysvalues);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     设置key的值 并返回一个旧值
     * </p>
     * @param key
     * @param value
     * @return 旧值 如果key不存在 则返回null
     */
    public String getset(String key, String value){
        Jedis jedis = null;
        String res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.getSet(key, value);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过下标 和key 获取指定下标位置的value
     * </p>
     * @param key
     * @param startOffset 开始位置 从0开始 负数表示从右边开始截取
     * @param endOffset
     * @return 如果没有返回null
     */
    public String getrange(String key, int startOffset, int endOffset){
        Jedis jedis = null;
        String res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.getrange(key, startOffset, endOffset);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     对value进行值+1操作 当value不是int类型时返回错误 当key不存在时则value为1
     * </p>
     * @param key
     * @return 加值后的结果
     */
    public Long incr(String key){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.incr(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key给指定的value加值 如果key不存在 则为该key的value值
     * </p>
     * @param key
     * @param integer
     * @return
     */
    public Long incrBy(String key, Long integer){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.incrBy(key, integer);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     对key的值做-1操作,如果key不存在,则设置key为-1
     * </p>
     * @param key
     * @return
     */
    public Long desc(String key) {
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.decr(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     减去指定的值
     * </p>
     * @param key
     * @param integer
     * @return
     */
    public Long decrBy(String key, Long integer) {
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.decrBy(key, integer);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key获取value值的长度
     * </p>
     * @param key
     * @return
     */
    public Long serlen(String key){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.strlen(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key给field设置指定的值 如果key不存在 则先创建
     * </p>
     * @param key
     * @param field 字段
     * @param value
     * @return 如果存在返回0 异常返回null
     */
    public Long hset(String key, String field, String value){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.hset(key, field, value);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     如果key不存在则创建 并给filed设置值为value 如果field已经存在 则操作无效
     * </p>
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hsetnx(String key, String field, String value){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.hsetnx(key, field, value);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key同时设置 hash的多个field
     * </p>
     * @param key
     * @param hash
     * @param indexdb 正常返回ok 异常返回null
     * @return
     */
    public String hmset(String key, Map<String, String> hash, int indexdb){
        Jedis jedis = null;
        String res = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexdb);
            res = jedis.hmset(key, hash);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key和field获取指定的value
     * </p>
     * @param key
     * @param field
     * @return 没有则返回null
     */
    public String hget(String key, String field){
        Jedis jedis = null;
        String res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.hget(key, field);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key和fields 获取指定的value 如果没有对应的value则返回null
     * </p>
     * @param key
     * @param indexdb
     * @param fields
     * @return
     */
    public List<String> hmget(String key, int indexdb, String... fields){
        Jedis jedis = null;
        List<String> res = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexdb);
            res = jedis.hmget(key, fields);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key给指定的field的value加上给定的值
     * </p>
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hincrBy(String key, String field, Long value){
        Jedis jedis = null;
        Long res = 0L;
        try{
            jedis = jedisPool.getResource();
            res = jedis.hincrBy(key, field, value);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key和filed判断是否有指定的value存在
     * </p>
     * @param key
     * @param field
     * @return 不存在-false  存在-true
     */
    public Boolean hexists(String key, String field){
        Jedis jedis = null;
        Boolean res = false;
        try{
            jedis = jedisPool.getResource();
            res = jedis.hexists(key, field);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key返回field的数量
     * </p>
     * @param key
     * @return
     */
    public Long hlen(String key){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.hlen(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key删除指定的filed
     * </p>
     * @param key
     * @param fields 可以是一个field 也可以是一个数组
     * @return
     */
    public Long hdel(String key, String... fields){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.hdel(key, fields);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key返回所有的field
     * </p>
     * @param key
     * @return
     */
    public Set<String> hkeys(String key){
        Jedis jedis = null;
        Set<String> res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.hkeys(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key返回所有和key有关的value
     * </p>
     * @param key
     * @return
     */
    public List<String> hvals(String key){
        Jedis jedis = null;
        List<String> res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.hvals(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key获取所有的field和value
     * </p>
     * @param key
     * @param indexdb
     * @return
     */
    public Map<String, String> hgetall(String key, int indexdb){
        Jedis jedis = null;
        Map<String, String> res = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexdb);
            res = jedis.hgetAll(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key向list头部添加字符串
     * </p>
     * @param indexdb
     * @param key
     * @param strs 可以是一个string 也可以是string数组
     * @return 返回list的value个数
     */
    public Long lpush(int indexdb, String key, String... strs){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexdb);
            res = jedis.lpush(key, strs);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return  res;
    }

    /**
     * <p>
     *     通过key向list尾部添加字符串
     * </p>
     * @param key
     * @param strs 可以是一个string 也可以是string数组
     * @return 返回list的value个数
     */
    public Long rpush(String key, String... strs){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.rpush(key, strs);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key在list指定的位置之前或者之后 添加字符串元素
     * </p>
     * @param key
     * @param where ListPosition枚举类型
     * @param pivot list里面的value
     * @param value 添加的value
     * @return
     */
    public Long linsert(String key, ListPosition where, String pivot, String value){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.linsert(key, where, pivot, value);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key设置指定下标位置的value
     * </p>
     * <p>
     *     如果下标超过list里面value的个数则报错
     * </p>
     * @param key
     * @param index 从0开始
     * @param value
     * @return 成功返回ok
     */
    public String lset(String key, Long index, String value){
        Jedis jedis = null;
        String res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.lset(key, index,value);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key从对应的list中删除指定的count个 和value相同的元素
     * </p>
     * @param key
     * @param count 当count为0时删除全部
     * @param value
     * @return 返回删除的个数
     */
    public Long lrem(String key, long count, String value){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.lrem(key, count, value);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key保留list中从start下标开始到end下标结束的value值
     * </p>
     * @param key
     * @param start
     * @param end
     * @return 成功返回ok
     */
    public String ltrim(String key, long start, long end){
        Jedis jedis = null;
        String res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.ltrim(key, start, end);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key从list尾部删除一个value 并返回该元素
     * </p>
     * @param key
     * @param indexdb
     * @return
     */
    synchronized public String rpop(String key, int indexdb){
        Jedis jedis = null;
        String res = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexdb);
            res = jedis.rpop(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key从一个list的尾部删除一个value并添加到另一个list头部 并返回该value
     * </p>
     * <p>
     *     如果第一个list为空或者不存在则返回null
     * </p>
     * @param scrkey
     * @param dstkey
     * @param indexdb
     * @return
     */
    public String rpoplpush(String scrkey, String dstkey, int indexdb){
        Jedis jedis = null;
        String res = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexdb);
            res = jedis.rpoplpush(scrkey, dstkey);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key获取list中指定下标位置的value
     * </p>
     * @param key
     * @param index
     * @return 如果返回null
     */
    public String lindex(String key, long index){
        Jedis jedis = null;
        String res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.lindex(key, index);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key返回list的长度
     * </p>
     * @param key
     * @return
     */
    public Long llen(String key){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.llen(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key获取list指定下标位置的value
     * </p>
     * <p>
     *     如果start为0 end为-1 则返回全部的list的value
     * </p>
     * @param key
     * @param start
     * @param end
     * @param indexdb
     * @return
     */
    public List<String> lrange(String key, long start, long end, int indexdb){
        Jedis jedis = null;
        List<String> res = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexdb);
            res = jedis.lrange(key, start, end);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     将列表key 下标为index的元素的值设置为value
     * </p>
     * @param key
     * @param index
     * @param value
     * @return
     */
    public String lset(String key, long index, String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.lset(key, index, value);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return null;
    }

    /**
     * <p>
     *     给定排序后的结果
     * </p>
     * @param key
     * @param sortingParams
     * @return 返回列表形式的排序结果
     */
    public List<String> sort(String key, SortingParams sortingParams){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.sort(key, sortingParams);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return null;
    }

    /**
     * <p>
     *     返回排序后的结果,排序默认以数字作为对象,值被解释为双精度浮点数,然后进行比较
     * </p>
     * @param key
     * @return 返回列表形式的排序结果
     */
    public List<String> sort(String key) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.sort(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return null;
    }

    /**
     * <p>
     *     通过key向指定的set中添加value
     * </p>
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return 添加成功的个数
     */
    public Long sadd(String key, String... members){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.sadd(key, members);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key删除set中对应的value值
     * </p>
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return 删除的个数
     */
    public Long srem(String key, String... members){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.srem(key, members);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key随机删除一个set中的value并返回该值
     * </p>
     * @param key
     * @return
     */
    public String spop(String key){
        Jedis jedis = null;
        String res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.spop(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key获取set中的差集
     * </p>
     * <p>
     *     以第一个set为标准
     * </p>
     * @param keys 可以使一个String 则返回set中所有的value 也可以是string数组
     * @return 返回差集
     */
    public Set<String> sdiff(String... keys){
        Jedis jedis = null;
        Set<String> res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.sdiff(keys);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key获取set中的差集并存入到另一个key中
     * </p>
     * <p>
     *     以第一个set为标准
     * </p>
     * @param dstkey 差集存入的key
     * @param keys 可以是一个string 则返回set中所有的value 也可以是string数组
     * @return
     */
    public Long sdiffstore(String dstkey, String... keys){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.sdiffstore(dstkey, keys);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    public Set<String> sinter(String... keys) {
        Jedis jedis = null;
        Set<String> res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.sinter(keys);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key获取指定set中的交集 并将结果存入新的set中
     * </p>
     * @param dstkey
     * @param keys 可以是一个string 也可以是一个string数组
     * @return
     */
    public Long sinterstore(String dstkey, String... keys){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.sinterstore(dstkey, keys);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key返回所有set的交集
     * </p>
     * @param keys 可以是一个string 也可以是一个string数组
     * @return
     */
    public Set<String> sunion(String... keys){
        Jedis jedis = null;
        Set<String> res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.sunion(keys);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key返回所有set的并集,并存入到新的set中
     * </p>
     * @param dstkey
     * @param keys 可以是一个string 也可以是一个string数组
     * @return
     */
    public Long sunionstore(String dstkey, String... keys){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.sunionstore(dstkey, keys);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key将set中的value移除并添加到第二个set中
     * </p>
     * @param srckey 需要移除的
     * @param dstkey 需要添加的
     * @param member set中的value
     * @return
     */
    public Long smove(String srckey, String dstkey, String member){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.smove(srckey, dstkey, member);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key获取set中value的个数
     * </p>
     * @param key
     * @return
     */
    public Long scard(String key){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.scard(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key判断value是否是set中的元素
     * </p>
     * @param key
     * @param member
     * @return
     */
    public Boolean sismember(String key, String member){
        Jedis jedis = null;
        Boolean res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.sismember(key, member);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key获取set中随机的value, 不删除元素
     * </p>
     * @param key
     * @return
     */
    public String srandmember(String key) {
        Jedis jedis = null;
        String res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.srandmember(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key获取set中所有的value
     * </p>
     * @param key
     * @return
     */
    public Set<String> smembers(String key){
        Jedis jedis = null;
        Set<String> res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.smembers(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    public Long zadd(String key, double score, String member){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.zadd(key, score, member);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     返回有序集 key中 指定区间内的成员 min=0 max=-1代表所有元素
     * </p>
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<String> zrange(String key, long min, long max){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.zrange(key, min, max);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return null;
    }

    /**
     * <p>
     *     统计有序集key中 值在Min和max之间的成员的数量
     * </p>
     * @param key
     * @param min
     * @param max
     * @return 值在min和max之间的成员的数量 异常返回0
     */
    public Long zcount(String key, double min, double max){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.zcount(key, min, max);
        }catch (Exception e){
            log.error(e.getMessage());
            return 0L;
        }finally {
            jedis.close();
        }
    }

    /**
     * <p>
     *     为hash表 key 中的域field的值加上增量increment 增量也可以为负数 相当于对给定域进行减法操作
     * </p>
     * <p>
     *     如果key不存在 一个新的hash表被创建并执行hincrBy命令 如果域field不存在 那么在执行命令前 域的值被初始化为0
     * </p>
     * <p>
     *     对储存字符串值的域field执行hincrBy命令将造成一个错误 本操作的值被限制在64位(bit)有符号数字表示之内
     * </p>
     * <p>
     *     将名称为key的hash中field的value增加integer
     * </p>
     * @param key
     * @param value
     * @param increment
     * @return 执行hincrBy命令之后 hash表key中域field的值 异常返回0
     */
    public Long hincrBy(String key, String value, long increment){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.hincrBy(key, value, increment);
        }catch (Exception e){
            log.error(e.getMessage());
            return 0L;
        }finally {
            jedis.close();
        }
    }

    /**
     * <p>
     *     通过key删除在zset中指定的value
     * </p>
     * @param key
     * @param members 可以是一个string 也可以是一个string数组
     * @return
     */
    public Long zrem(String key, String... members){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.zrem(key, members);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key增加该zset中value的score的值
     * </p>
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Double zincrby(String key, double score, String member){
        Jedis jedis = null;
        Double res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.zincrby(key, score, member);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key返回zset中value的排名
     * </p>
     * <p>
     *     下标从小到大排序
     * </p>
     * @param key
     * @param member
     * @return
     */
    public Long zrank(String key, String member){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.zrank(key, member);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key返回zset中value的排名
     * </p>
     * <p>
     *     下标从大到小排序
     * </p>
     * @param key
     * @param member
     * @return
     */
    public Long zrevrank(String key, String member){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.zrevrank(key, member);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key将获取score从start到end中zset的value
     * </p>
     * <p>
     *     score从大到小排序
     * </p>
     * <p>
     *     当start为0 end为-1时返回全部
     * </p>
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrevrange(String key, long start, long end){
        Jedis jedis = null;
        Set<String> res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.zrevrange(key, start, end);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key返回指定score内zset中的value
     * </p>
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<String> zrangebyscore(String key, String min, String max){
        Jedis jedis = null;
        Set<String> res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.zrevrangeByScore(key, min, max);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key返回指定score内zset中的value
     * </p>
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<String> zrangeByScore(String key, double min, double max){
        Jedis jedis = null;
        Set<String> res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.zrangeByScore(key, min, max);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     返回指定区间内zset中value的数量
     * </p>
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zcount(String key, String min, String max){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.zcount(key, min, max);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key返回zset中的value个数
     * </p>
     * @param key
     * @return
     */
    public Long zcard(String key){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.zcard(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key获取zset中value的score值
     * </p>
     * @param key
     * @param member
     * @return
     */
    public Double zscore(String key, String member){
        Jedis jedis = null;
        Double res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.zscore(key, member);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    public Long zremrangeByRank(String key, long start, long end){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.zremrangeByRank(key, start, end);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     通过key删除指定score内的元素
     * </p>
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByScore(String key, double start, double end){
        Jedis jedis = null;
        Long res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.zremrangeByScore(key, start, end);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    /**
     * <p>
     *     返回满足pattern表达式的所有key
     * </p>
     * <p>
     *     keys(*)
     * </p>
     * <p>
     *     返回所有的key
     * </p>
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern){
        Jedis jedis = null;
        Set<String> res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.keys(pattern);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }

    public Set<String> keysBySelect(String pattern, int database){
        Jedis jedis = null;
        Set<String> res = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(database);
            res = jedis.keys(pattern);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }


    /**
     * <p>
     *     通过key判断值的类型
     * </p>
     * @param key
     * @return
     */
    public String type(String key){
        Jedis jedis = null;
        String res = null;
        try{
            jedis = jedisPool.getResource();
            res = jedis.type(key);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return res;
    }


}
