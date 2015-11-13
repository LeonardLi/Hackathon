package com.cloudteam.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.sf.json.JSONObject;
import com.cloudteam.utils.DButils;
import com.cloudteam.utils.*;
import com.cloudteam.hackathonServer.*;

public class Login  {
	public String return_info = null;
	public Login() {
		// TODO Auto-generated constructor stub
		super();
	}

	public int LoginHand(String login_info)  {
		
		if(login_info.equals(null))
			return newServer.ErrorCode_EMPTY_REQUEST;
		try {
			JSONObject jsonobj = new JSONObject();
			jsonobj = JSONObject.fromObject(login_info);
			String username = jsonobj.getString("username");
			String password = jsonobj.getString("password");
			Connection connection = DButils.getConnection(true);
			try {
				StringBuilder sql = new StringBuilder();
				sql.append("select * from user where name='").append(username)
						.append("' && password='").append(password).append("'");
				Statement statement = connection.createStatement();
				ResultSet resultSet = (ResultSet) statement.executeQuery(sql
						.toString());
				if (resultSet.next()) {
					return_info = "{\n" + "\"user_id\": "
							+ resultSet.getString("id") + ",\n\"username\": \""
							+ resultSet.getString("name")
							+ "\",\n\"access_token\": \"" + TokenGenerator.getInstance().TokenMap.get(Integer.parseInt(resultSet.getString("id"))) + "\"\n}";
				} else{
					return newServer.ErrorCode_USER_AUTH_FAIL;   //用户名或密码错误
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DButils.close(connection);
			}

		} catch (Exception e) { 
			return newServer.ErrorCode_MALFORMED_JSON;  //格式错误
		}
		return newServer.SuccessCode_LOGIN;
	}
}
