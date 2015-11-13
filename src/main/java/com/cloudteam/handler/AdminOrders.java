package com.cloudteam.handler;

public class AdminOrders {
	public AdminOrders() {
		// TODO Auto-generated constructor stub
	}
	
	public String QueryAdminOrdersHand(String access_token) {
		String return_info = null;
		//开始查询
		//............................
		return_info  = "[{\"id\":\"someorderid\",\"user_id\": 1,\"items\":[{\"food_id\": 2, \"count\": 1}],\"total\": 10}]";
		System.out.println(return_info);
		return return_info;
	}
}
