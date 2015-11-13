package com.cloudteam.handler;

import com.cloudteam.hackathonServer.newServer;

import net.sf.json.JSONObject;

public class Order {

	public String return_info = null;

	public Order() {
		// TODO Auto-generated constructor stub
	}

	public int OrderHand(String order_info,String access_token){
		String return_info = null;
		//开始判断食物
		/*POST /orders?access_token=xxx
{
    "cart_id": "e0c68eb96bd8495dbb8fcd8e86fc48a3"
}*/
		try {
			JSONObject jsonobj = new JSONObject();
			jsonobj = JSONObject.fromObject(order_info);
			String cart_id = jsonobj.getString("cart_id");
			if(true)  //篮子存在
			{
				if(true)  //篮子是他的
				{
					if(true)  //还未下单
					{
						if(true)  //食物库存足
						{
							//创建订单，清空篮子
							return_info = "{\"id\":\"someorderid\"}";
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
		} catch (Exception e) { 
				return newServer.ErrorCode_MALFORMED_JSON;  //JSON格式错误
		}
		return newServer.SuccessCode;
	}
}
