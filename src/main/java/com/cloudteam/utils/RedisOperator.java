package com.cloudteam.utils;

/***
 * 
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RedisOperator {

	static String sql = null;
	static DBHelper db1 = null;
	static ResultSet ret = null;
	private RedisClient client = null;
	static Integer[] Price = new Integer[101]; // 把价钱存在内存里

	public static void main(String[] args) {
		RedisOperator a = new RedisOperator();
		a.copy2Redis();
		// System.out.println(a.checkload());
		System.out.println();
	}

	public RedisOperator() {
		this.client = new RedisClient();
	}

	public void copy2Redis() {

		if (client.shardedJedis.hexists("Amounts", "1")) {
			return;
		} else {
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
	}

	public void copyToken2Redis(Map<Integer, String> tokenMap) {
		for (int i = 1; i < 1001; i++) {
			client.shardedJedis.hset("Token", String.valueOf(i),
					tokenMap.get(i));
		}
	}

	public HashMap<Integer, String> getTokenMap() {
		HashMap<Integer, String> hash = new HashMap<Integer, String>();
		for (int i = 1; i < 1001; i++) {
			hash.put(i, client.shardedJedis.hget("Token", String.valueOf(i)));
		}
		return hash;
	}

	// 购物车存在与否
	public boolean isCartsExist(String cart_id) {
		return client.shardedJedis.hexists("Carts", cart_id);
	}

	// 获取订单信息
	public String getOrder(String order_id) {
		JSONArray result = new JSONArray();
		JSONObject js = new JSONObject();
		if (client.shardedJedis.hget("Orders", order_id) != null) {
			js = JSONObject.fromObject(client.shardedJedis.hget("Orders",
					order_id));
			result.add(js);
			return result.toString();
		} else
			return "[]"; // 返回空
		// 返回为JSONArray格式
	}

	// 管理员获取订单信息
	public String getAdminOrder() {
		// 开始构造返回信息
		JSONArray allorder_info = new JSONArray();
		JSONObject order_info = new JSONObject();

		Map<String, String> result = client.shardedJedis.hgetAll("Orders");
		if (result != null) {
			for (Map.Entry<String, String> entry : result.entrySet()) {
				int user_id = TokenGenerator.getInstance().Token2User.get(entry
						.getKey().toString());
				order_info = JSONObject.fromObject(entry.getValue()); // 先拿过来数据
				order_info.put("user_id", user_id); // user_id;
				allorder_info.add(order_info);
			}
			System.out.println(allorder_info.toString());
			return allorder_info.toString();
		} else
			return "[]"; // 空订单
	}

	// 订单是否创建过
	public boolean checkOrders(String cart_td) {
		return client.shardedJedis.hexists("Orders", cart_td);
	}

	// 食物库存足不足
	public boolean checkAmout(String str_json) {
		try {
			JSONObject json_temp = new JSONObject();
			json_temp = JSONObject.fromObject(str_json);
			String cart_id = json_temp.getString("cart_id");
			String cart_info = client.shardedJedis.hget("Carts", cart_id);
			JSONArray ja = new JSONArray();
			ja = JSONArray.fromObject(cart_info);
			for (int i = 0; i < ja.size(); i++) {
				int order_amount = JSONObject.fromObject(ja.get(i)).getInt(
						"count");
				String amount = client.shardedJedis.hget("Amounts", JSONObject
						.fromObject(ja.get(i)).getString("food_id"));
				if (order_amount > 1000
						|| Integer.parseInt(amount) - order_amount < 0) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * @SuppressWarnings("unchecked") public void add2Carts(String cart_id ,int
	 * food_id, int count){ //只修改篮子不修改库存 String result =
	 * client.shardedJedis.hget("Carts",cart_id); JSONArray items = new
	 * JSONArray(); JSONObject cart = new JSONObject(); cart =
	 * JSONObject.fromObject(result); items = cart.getJSONArray("items");
	 * for(int i = 0;i<items.size();i++){ JSONObject item =
	 * (JSONObject)items.get(i);
	 * if(Integer.parseInt(item.get("food_id").toString()) == food_id){ int
	 * orgin = item.getInt("count"); item.replace("count", orgin+count); }else {
	 * JSONObject newItem = new JSONObject(); newItem.put("food_id", food_id);
	 * newItem.put("count", count); items.add(newItem); } } //not finished //
	 * "total" not add //
	 * 
	 * 
	 * }
	 */
	/**
	 * 
	 * @return carts_id
	 */
	public String createCarts(String token) {
		// 创建篮子
		// int carts_id = TokenGenerator.getInstance().Token2User.get(token);
		if (client.shardedJedis.hget("Carts", token) == null)
			client.shardedJedis.hset("Carts", token, ""); // token暂时设计为篮子ID
		return token;
	}

	@SuppressWarnings("unchecked")
	public boolean addFoodToCarts(String carts_id, JSONObject food) {
		JSONObject food_add = new JSONObject();
		food_add = food;
		int food_id = food_add.getInt("food_id"); // 食物ID
		int count = food_add.getInt("count"); // 食物数量
		// 添加到购物车的单个食物是否超过3个
		if (count > 3)
			return false;

		String result = client.shardedJedis.hget("Carts", carts_id);

		// 准备插入redis
		JSONArray food_items = new JSONArray();
		if (result.equals("")) { // 第一次添加，先构造
			food_items.add(food_add); // 第一种食物信息
			System.out.println("cart_info:............" + food_items.toString()
					+ "\n");
			client.shardedJedis.hset("Carts", carts_id, food_items.toString()); // 插入redis,格式为JSONArray
			return true;
		} else { // 不是第一次添加，取出数据再直接加入食物
			int total_count = 0; // 当前食物总量
			JSONObject jo = new JSONObject();
			food_items = JSONArray.fromObject(result); // 把原来的数据转为JSONArray
			int i = 0;
			for (i = 0; i < food_items.size(); i++) {
				jo = (JSONObject) food_items.get(i);
				total_count += jo.getInt("count");
			}
			if (count + total_count > 3) { // 食物数量加上现在这个超过三，返回错误
				return false;
			} else {
				// 如果这种食物已经有了，则只更新数量
				for (i = 0; i < food_items.size(); i++) { // 遍历JSONArray
					jo = (JSONObject) food_items.get(i);
					if (jo.getInt("food_id") == food_id) // 如果要添加的食物已经在购物车了，则更改数量
						break;
				}
				if (i == food_items.size()) // 并没有该食物
				{
					if (count > 0)
						food_items.add(food_add); // 加入现在插入的食物信息
				} else {
					if (count + jo.getInt("count") <= 0) // 如果个数小于零就remove
					{
						food_items.remove(i);
					} else // 如果个数大于零，就更新
					{
						jo.put("count", count + jo.getInt("count")); // 更新数量
						food_items.set(i, jo); // 更新这条数据
					}
				}
				System.out.println("cart_info:............"
						+ food_items.toString() + "\n");
				client.shardedJedis.hset("Carts", carts_id,
						food_items.toString()); // 插入redis,格式为JSONArray
				return true;
			}
		}
	}

	/*
	 * POST /orders?access_token=xxx { "cart_id":
	 * "e0c68eb96bd8495dbb8fcd8e86fc48a3" }
	 */
	// 创建订单
	public void createOrder(String access_token, String carts_id) {
		String order_id = access_token;

		Connection cn = null;
		try {
			cn = DButils.getConnection();

			// 查询cart_info
			JSONArray cart_info = new JSONArray();
			cart_info = JSONArray.fromObject(client.shardedJedis.hget("Carts",
					carts_id)); // 拿到cart_info数据，JSONArray
			int price = 0;
			JSONObject jo = new JSONObject();
			for (int i = 0; i < cart_info.size(); i++) {
				jo = JSONObject.fromObject(cart_info.get(i)); // 一个一个拿数据
				price += jo.getInt("count") * (Price[jo.getInt("food_id")]); // 算价钱
																				// 单价乘以数量
				// 改变库存
				String instantCount= String.valueOf((Integer.parseInt(client.shardedJedis
						.hget("Amounts", jo.getString("food_id"))) - jo
						.getInt("count")));
				client.shardedJedis.hset("Amounts", jo.getString("food_id"),
						instantCount);
				sql = "update food set stock=" + instantCount
						+ " where id=" + jo.getInt("food_id");

				PreparedStatement ps = cn.prepareStatement(sql);
				ps.executeUpdate();

				cn.commit();

				JSONObject order_info = new JSONObject();
				order_info.put("id", access_token);
				order_info.put("items", cart_info);
				order_info.put("total", price);

				client.shardedJedis.hset("Orders", order_id,
						order_info.toString());  // 插入orders,类型为JSONObject
				// 关闭连接
				cn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				cn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public boolean checkLoadToken() {
		boolean isExist = client.shardedJedis.hexists("Token", "1");
		return isExist;
	}

	public boolean checkLoadAmount() {
		boolean isExist = client.shardedJedis.hexists("Amounts", "1");
		return isExist;
	}
}
