package com.cloudteam.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FoodsTableRedis {

	static String sql = null;
	static DBHelper db1 = null;
	static ResultSet ret = null;

	public static void main(String[] args) {
		RedisClient FoodsTable_Redis = new RedisClient();
		sql = "select *from food";//SQL语句
		db1 = new DBHelper(sql);//创建DBHelper对象
		try {
			ret = db1.pst.executeQuery();//执行语句，得到结果集
			while (ret.next()) {
				FoodsTable_Redis.shardedJedis.hincrBy("FoodsTable",ret.getString(1),Integer.parseInt(ret.getString(2)));
			}//显示数据
			ret.close();
			db1.close();//关闭连接
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
