package com.cloudteam.handler;


public class Orders {

	public Orders() {
		// TODO Auto-generated constructor stub
	}
	
	public String QueryOrdersHand(String access_token) {
		/*GET /orders?access_token=xxx*/
		String return_info = null;
		//开始查询
		//............................
		return_info  = "[{\"id\":\"someorderid\",\"items\":[{\"food_id\": 2, \"count\": 1}],\"total\": 10}]";
		System.out.println(return_info);
		return return_info;
	}
}
