package com.cloudteam.utils;
/***
 * 
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RedisOperator {

	static String sql = null;
	static DBHelper db1 = null;
	static ResultSet ret = null;
	private RedisClient client = null;
	static Integer[] Price = new Integer[1001];
	
	public static void main(String[] args){
		RedisOperator a = new RedisOperator();
		a.copy2Redis();
		//System.out.println(a.checkload());
		System.out.println();
	}

	public RedisOperator() {
		this.client = new RedisClient();
	}

	public void copy2Redis() {
		
		sql = "select *from food";// SQL语句
		db1 = new DBHelper(sql);// 创建DBHelper对象
		try {
			ret = db1.pst.executeQuery();// 执行语句，得到结果集
			int i = 0;
			while (ret.next()) {
				client.shardedJedis.hset("Amounts", ret.getString(1),
						ret.getString(2));
				Price[++i] = Integer.parseInt(ret.getString(3));
			} // 显示数据
			ret.close();
			db1.close();// 关闭连接
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void copyToken2Redis(Map<Integer, String> tokenMap){
		for(int i =1 ;i<1001;i++){
			client.shardedJedis.hset("Token", String.valueOf(i),tokenMap.get(i) );
		}		
	}
	
	public boolean checkLoadAmount(){
		boolean isExist = client.shardedJedis.hexists("Amounts", "1");
		return isExist;
	}
	
	public HashMap<Integer,String> getTokenMap(){
		HashMap<Integer,String> hash = new HashMap<Integer,String>(); 
		for(int i = 1; i<1001;i++){
			hash.put(i, client.shardedJedis.hget("Token", String.valueOf(i)));
		}	
		return hash;		
	}
	public void reduceAmount(){
		//下单成功,
		
	}
	
	public void add2Carts(String cart_id ,int food_id, int count){
		//只修改篮子不修改库存
	}
	public String createCarts(){
		//创建篮子
		return null;
	}
	
	public boolean isOverLimits(int food_id, int food_amount){
		//添加到购物车的食物是否超过3个
		return true;
	}
	
	public boolean checkLoadToken(){
		boolean isExist = client.shardedJedis.hexists("Token", "1");
		return isExist;
	}
}
