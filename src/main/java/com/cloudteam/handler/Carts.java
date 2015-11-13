package com.cloudteam.handler;

import com.cloudteam.hackathonServer.newServer;
import com.cloudteam.utils.*;

public class Carts {

	public String return_info = null;
	public Carts() {
		// TODO Auto-generated constructor stub
	}

	public int CreateCartsHand(String access_token) {
		String return_info = null;
		int Status_Code = 200;   //状态码
		// 创建购物车
		// .............................
		return_info = "{\"cart_id\":\"e0c68eb96bd8495dbb8fcd8e86fc48a3\"}";

		return Status_Code;
	}
}
