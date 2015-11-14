package com.cloudteam.handler;

import com.cloudteam.utils.RedisOperator;

public class Carts {

	public String return_info = null;
	public Carts() {
		// TODO Auto-generated constructor stub
	}

	public String CreateCartsHand(String token) {
		String return_info = null;
		RedisOperator operator = new RedisOperator();
		
		String carts_id = operator.createCarts(token);
		return_info = "{\"cart_id\":\"" + carts_id +"\"}";
		return return_info;
	}
}
