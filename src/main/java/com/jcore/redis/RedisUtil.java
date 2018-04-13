package com.jcore.redis;

import redis.clients.jedis.*;




public final class RedisUtil {
    

    private static String ADDR = "127.0.0.1";
    

    private static int PORT = 6379;
    

    private static String AUTH = "admin";
    

    private static int MAX_ACTIVE = 1024;
    

    private static int MAX_IDLE = 200;
    

    private static int MAX_WAIT = 10000;
    
    private static int TIMEOUT = 10000;
    

    private static boolean TEST_ON_BORROW = true;
    
    private static JedisPool jedisPool = null;
    
 
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();           
 
            config.setMaxTotal(MAX_ACTIVE);             
            //config.setMaxActive(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            //config.setMaxWait(MAX_WAIT);
            config.setMaxWaitMillis(MAX_WAIT);            
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
 
    public synchronized  void returnResource( Jedis jedis) {
        if (jedis != null) {
            jedisPool.close();
        }
    }
    
    public synchronized void returnBrokenResource(Jedis jedis) {  
        if (jedis != null) {  
        	jedisPool.close();
        }  
    }  
}
