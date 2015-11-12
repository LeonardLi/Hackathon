package com.cloudteam.utils;

import redis.clients.jedis.Jedis;



public class RedisUtils {
	
	public RedisUtils(){
		connect();
	}
	
	private void connect(){
		Jedis jedis = new Jedis("127.0.0.1");
		System.out.println(jedis.ping());
	}
}
