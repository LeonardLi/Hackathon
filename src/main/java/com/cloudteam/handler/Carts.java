package com.cloudteam.handler;

import com.cloudteam.hackathonServer.newServer;
import com.cloudteam.utils.*;

public class Carts {

	public Carts() {
		// TODO Auto-generated constructor stub
	}

	public String CreateCartsHand() {
		String return_info = null;

		// 创建购物车
		// .............................
		System.out.println("Token Success");
		return_info = "{\"cart_id\": \"e0c68eb96bd8495dbb8fcd8e86fc48a3\"}";

		return return_info;
	}
}
