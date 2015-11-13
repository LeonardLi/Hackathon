package com.cloudteam.utils;


public class TokenTableRedis {

	public static void CreatBiTokenTable(String[] args) {
		RedisClient TokenTable_Redis = new RedisClient();
		for(int i=1;i<1001;i++){
			TokenTable_Redis.shardedJedis.hset("TokenTable_Usr_Token",String.valueOf(i),TokenGenerator.getInstance().TokenMap.get(i));
			TokenTable_Redis.shardedJedis.hset("TokenTable_Token_Usr",TokenGenerator.getInstance().TokenMap.get(i),String.valueOf(i));
		}
		
}
}