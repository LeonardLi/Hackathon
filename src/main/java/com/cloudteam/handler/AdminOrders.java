package com.cloudteam.handler;

import com.cloudteam.utils.RedisOperator;

public class AdminOrders {
	public AdminOrders() {
		// TODO Auto-generated constructor stub
	}
	
	public String QueryAdminOrdersHand(String access_token) {
		/*GET /admin/orders?access_token=xxx*/
		String return_info = null;
		RedisOperator operator = new RedisOperator();
		return_info = operator.getAdminOrder();
		return return_info;
	}
}
