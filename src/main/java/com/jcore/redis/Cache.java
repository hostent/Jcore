package com.jcore.redis;


 

public class Cache {

	 public static String get(String key)
	 {
		 return RedisUtil.getJedis().get(key);
	 }
	 
	 public static void set(String key,String val)
	 {
		 RedisUtil.getJedis().set(key, val);
	 }
	 
	 public static void set(String key,String val,int expireSecond)
	 {
		 RedisUtil.getJedis().set(key, val);
		 RedisUtil.getJedis().exists(key);
	 }
	 
	 public static void delete(String key)
	 {
		 RedisUtil.getJedis().del(key);
	 }
	 
	 public static void expire(String key,int expireSecond)
	 {
		 RedisUtil.getJedis().exists(key);
	 }
	 
	 public static void rpush(String key,String val)
	 {
		 RedisUtil.getJedis().rpush(key, val);
	 }
	 
	 public static String rpop(String key)
	 {
		return  RedisUtil.getJedis().rpop(key);
	 }
	 
	 public static String lpop(String key)
	 {
		return  RedisUtil.getJedis().lpop(key);
	 }
	 

	 
	 
	 
}
