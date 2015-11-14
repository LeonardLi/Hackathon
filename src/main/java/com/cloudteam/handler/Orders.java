package com.cloudteam.handler;

import com.cloudteam.utils.RedisOperator;
import com.cloudteam.utils.TokenGenerator;


public class Orders {

	public Orders() {
		// TODO Auto-generated constructor stub
	}
	
	public String QueryOrdersHand(String access_token) {
		/*GET /orders?access_token=xxx*/
		String return_info = null;
		int order_id = TokenGenerator.getInstance().Token2User.get(access_token);
		RedisOperator operator = new RedisOperator();
		return_info = operator.getOrder(String.valueOf(order_id));
		//return_info  = "[{\"id\":\"someorderid\",\"items\":[{\"food_id\": 2, \"count\": 1}],\"total\": 10}]";
		//System.out.println(return_info);
		return return_info;
	}
}
