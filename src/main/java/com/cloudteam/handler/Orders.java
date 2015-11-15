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
		String order_id = access_token;  //token暂时为order_id
		RedisOperator operator = new RedisOperator();
		return_info = operator.getOrder(order_id);
		return return_info;
	}
}
