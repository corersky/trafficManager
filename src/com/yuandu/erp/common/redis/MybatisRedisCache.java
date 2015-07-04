package com.yuandu.erp.common.redis;

import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

import com.yuandu.erp.common.utils.JedisUtils;

/**
 * 使用第三方缓存服务器，处理二级缓存
 * @author 
 */
public class MybatisRedisCache implements Cache {
    
    private static final Logger logger = Logger.getLogger(MybatisRedisCache.class);
    
    private String id;
     
    /** The ReadWriteLock. */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
 
    private final String COMMON_CACHE_KEY = "ERP:";
    
    public MybatisRedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("必须传入ID");
        }
        this.id = id;
    }
  
    /**
     * 按照一定规则标识key
     */
    private String getKey(Object key) {
        StringBuilder accum = new StringBuilder();
        accum.append(COMMON_CACHE_KEY);
        accum.append(this.id).append(":");
        accum.append(DigestUtils.md5Hex(String.valueOf(key)));
        return accum.toString();
    }
  
    /**
     * redis key规则前缀
     */
    private String getKeys() {
        return COMMON_CACHE_KEY + this.id + ":*";
    }
 
    @Override
    public String getId() {
        return this.id;
    }
 
    @Override
    public int getSize() {
        Jedis jedis = null;
        int result = 0;
        try {
            jedis = JedisUtils.getResource();
            Set<byte[]> keys = jedis.keys(JedisUtils.getBytesKey(getKeys()));
            if (null != keys && !keys.isEmpty()) {
                result = keys.size();
            }
        } catch (Exception e) {
            logger.error("mybatis jedis getSize error!");
        } finally{
        	if(jedis!=null)
        		JedisUtils.returnResource(jedis);
        }
        return result;
 
    }
 
    @Override
    public void putObject(Object key, Object value) {
        try {
            JedisUtils.setObject(getKey(key), value, 0);
        } catch (Exception e) {
        	logger.error("mybatis jedis putObject error!",e);
        }
 
    }
 
    @Override
    public Object getObject(Object key) {
        Object value = null;
        try {
        	value = JedisUtils.getObject(getKey(key));
        } catch (Exception e) {
        	logger.error("mybatis jedis getObject error!",e);
        } 
        return value;
    }
 
    @Override
    public Object removeObject(Object key) {
        Object value = null;
        try {
        	value = JedisUtils.delObject(getKey(key));
            logger.debug("LRU算法从缓存中移除-----"+this.id);
        } catch (Exception e) {
        	logger.error("mybatis jedis removeObject error!",e);
        } 
        return value;
    }
 
    @Override
    public void clear() {
        Jedis jedis = null;
        try {
            jedis = JedisUtils.getResource();
            Set<byte[]> keys = jedis.keys(JedisUtils.getBytesKey(getKeys()));
            for (byte[] key : keys) {
                jedis.del(key);
            }
        } catch (Exception e) {
        	logger.error("mybatis jedis clear error!",e);
        } finally{
        	if(jedis!=null)
        		JedisUtils.returnResource(jedis);
        }
    }
 
    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }
     
}