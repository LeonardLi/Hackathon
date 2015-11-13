package com.cloudteam.handler;

import com.cloudteam.hackathonServer.newServer;

import net.sf.json.JSONObject;

public class OrderHandler extends MyHandler{

	public OrderHandler() {
		// TODO Auto-generated constructor stub
	}
	public String OrderHand(String order_info,String access_token){
		String return_info = null;
		/*if(access_token.equals(null))
			return newServer.ErrorCode_INVALID_ACCESS_TOKEN;  //token为空
		if(order_info.equals(null))
			return newServer.ErrorCode_EMPTY_REQUEST;  //请求为空
		//开始判断食物
		try {
			JSONObject jsonobj = new JSONObject();
			jsonobj = JSONObject.fromObject(order_info);
			String cart_id = jsonobj.getString("cart_id");
//			if()  //cart存在
//			{
//				
//			}else
//				return super.ErrorCode_CART_NOT_FOUND;  //篮子不存在
		} catch (Exception e) { 
				return newServer.ErrorCode_MALFORMED_JSON;  //格式错误
		}
		
		
		//判断篮子是否存在
		if(true)  //存在
		{
			//判断篮子是不是他的,篮子的access_token和传过来的accesstoken对比
			if(true)  //篮子是他的
			{
				if(true)  //篮子数量不超出三个
				{
					
				}else
					{
					return newServer.ErrorCode_FOOD_OUT_OF_LIMIT;
					}
			}else
			{
				return newServer.ErrorCode_NOT_AUTHORIZED_TO_ACCESS_CART;
			}
		}else
		{
			return newServer.ErrorCode_CART_NOT_FOUND;
		}*/
		return return_info;
	}
}
