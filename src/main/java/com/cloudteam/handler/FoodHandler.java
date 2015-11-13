package com.cloudteam.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cloudteam.hackathonServer.newServer;
import com.cloudteam.utils.DButils;
import com.cloudteam.utils.TokenGenerator;

import net.sf.json.JSONObject;

public class FoodHandler extends MyHandler {

	public FoodHandler() {
		// TODO Auto-generated constructor stub
	}

	public String AddFoodHand(String food_info, String access_token, String cart_id){
		String return_info = null;
		/*if(food_info.equals(null))
			return newServer.ErrorCode_EMPTY_REQUEST;  //空请求
		if(access_token.equals(null))
			return newServer.ErrorCode_INVALID_ACCESS_TOKEN;  //token为空
		if(cart_id.equals(null) || cart_id.equals(""))
			return newServer.ErrorCode_CART_NOT_FOUND;  //篮子不存在
		//判断篮子是否存在
		if(true)  //存在
		{
			//判断篮子是不是他的,篮子的access_token和传过来的accesstoken对比
			if(true)  //篮子是他的
			{
				if(true)  //篮子数量不超出三个
				{
					//开始判断食物
					try {
						JSONObject jsonobj = new JSONObject();
						jsonobj = JSONObject.fromObject(food_info);
						String food_id = jsonobj.getString("food_id");
						String count = jsonobj.getString("count");
						//食物存在与否？
						if(true){  //存在
							//开始插入食物，更新购物车信息，此时还不更新食物库存
							//设置返回信息
							return_info = "204 No content";
						}else
						{
							return newServer.ErrorCode_FOOD_NOT_FOUND;
						}
					} catch (Exception e) { 
							return newServer.ErrorCode_MALFORMED_JSON;  //格式错误
					}
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
