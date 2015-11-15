package com.cloudteam.utils;
/***
 * 
 */
import java.awt.PageAttributes.OriginType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Templates;

import net.sf.json.JSONArray;
import net.sf.json.JSONFunction;
import net.sf.json.JSONObject;

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
	
	@SuppressWarnings("unchecked")
	public void add2Carts(String cart_id ,int food_id, int count){
		//只修改篮子不修改库存
		String result = client.shardedJedis.hget("Carts",cart_id);
		JSONArray items = new JSONArray();
		JSONObject cart = new JSONObject();
		cart = JSONObject.fromObject(result);
		items = cart.getJSONArray("items");
		for(int i = 0;i<items.size();i++){
			JSONObject item = (JSONObject)items.get(i);
			if(Integer.parseInt(item.get("food_id").toString()) == food_id){
				int orgin = item.getInt("count");
				item.replace("count", orgin+count);
			}else {
				JSONObject newItem = new JSONObject();
				newItem.put("food_id", food_id);
				newItem.put("count", count);
				items.add(newItem);
			}
		}
		//not finished 
		// "total" not add
		// 

		
	}
	
	/**
	 * 
	 * @return carts_id
	 */
	public String createCarts(String token){
		//创建篮子
		int carts_id = TokenGenerator.getInstance().Token2User.get(token);
		client.shardedJedis.hset("Carts", String.valueOf(carts_id), "");
		return String.valueOf(carts_id);
	}
	
	public boolean isCartsExist(String cart_id){
		return client.shardedJedis.hexists("Carts", cart_id);		
	}
	
	public void createOrder(String order_id,String data){
		client.shardedJedis.hset("Orders", order_id,data); 
	}
	
	public String getOrder(String order_id){
		String result=client.shardedJedis.hget("Orders", order_id);
		return result;
	}
	
    public boolean checkOrders(String cart_td){
    	 return client.shardedJedis.hexists("Orders", cart_td);   	
    }
    
    public boolean checkAmout(JSONObject json){
    	String food_id = json.get("food_id").toString();
    	int order_amount = json.getInt("count");
    	String amount = client.shardedJedis.hget("Amount", "food_id");
    	if(Integer.parseInt(amount)-order_amount < 0 || order_amount > 1000){
    		return false;
    	}
    	return true;
    }
		
	
	@SuppressWarnings("unchecked")
	public boolean isOverLimits(String carts_id,String food_id, int food_amount){
		//添加到购物车的食物是否超过3个
		if(food_amount > 3) return false;
		
		String result = client.shardedJedis.hget("Carts", carts_id);
		
		JSONObject foodresult =new JSONObject();
		
		if(result.equals("")){
			foodresult.put(food_id, food_amount);
			client.shardedJedis.hset("Carts", carts_id, foodresult.toString());
			return true;
		}else{			
			foodresult = JSONObject.fromObject(result);
			int count = foodresult.getInt(food_id);
			if(count+ food_amount > 3){
				return false;
			}else{
				foodresult.replace(food_id, count+food_amount);
				client.shardedJedis.hset("Carts", carts_id, foodresult.toString());
				return true;
			}
		}
		
	}
	
	public boolean checkLoadToken(){
		boolean isExist = client.shardedJedis.hexists("Token", "1");
		return isExist;
	}
	public boolean checkLoadAmount(){
		boolean isExist = client.shardedJedis.hexists("Amounts", "1");
		return isExist;
	}
}
