package com.cloudteam.handler;

import com.cloudteam.hackathonServer.newServer;
import com.cloudteam.utils.RedisOperator;
import com.cloudteam.utils.TokenGenerator;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class Order {

	public String return_info = null;

	public Order() {
		// TODO Auto-generated constructor stub
	}

	public int OrderHand(String order_info,String access_token){
		try {
			JSONObject jsonobj = new JSONObject();
			jsonobj = JSONObject.fromObject(order_info);
			String cart_id = jsonobj.getString("cart_id");  //篮子ID
			RedisOperator op = new RedisOperator();
			
			if(op.isCartsExist(cart_id))  //篮子存在
			{
				if(cart_id.equals(access_token))  //篮子是他的
				{
					if(!op.checkOrders(cart_id))  //还未下单
					{
						if(op.checkAmout(order_info))  //食物库存足
						{
							op.createOrder(access_token, cart_id); //传入购物车的信息下订单，已经更新了库存
							return_info = "{\"id\":\"" + access_token + "\"}";
						}
						else
						{
							return newServer.ErrorCode_FOOD_OUT_OF_STOCK;  //食物库存不足
						}
					}else
					{
						return newServer.ErrorCode_ORDER_OUT_OF_LIMIT;  //下单次数超限
					}
				}else
				{
					return newServer.ErrorCode_NOT_AUTHORIZED_TO_ACCESS_CART;  //无权访问篮子
				}
			}else
			{
				return newServer.ErrorCode_CART_NOT_FOUND;  //篮子不存在
			}
		} catch (JSONException e) { 
				return newServer.ErrorCode_MALFORMED_JSON;  //JSON格式错误
		}
 		return newServer.SuccessCode;
	}
}
