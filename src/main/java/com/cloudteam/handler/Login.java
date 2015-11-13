package com.cloudteam.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.sf.json.JSONObject;
import com.cloudteam.utils.DButils;
import com.cloudteam.utils.*;
import com.cloudteam.hackathonServer.*;

public class Login extends MyHandler {

	public Login() {
		// TODO Auto-generated constructor stub
		super();
	}

	public String LoginHand(String login_info) throws SQLException {
		String return_info = null;
		if(login_info.equals(null))
			return String.valueOf(newServer.ErrorInfo_EMPTY_REQUEST);
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
					return String.valueOf(newServer.ErrorInfo_USER_AUTH_FAIL);   //用户名或密码错误
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DButils.close(connection);
			}

		} catch (Exception e) { 
			return String.valueOf(newServer.ErrorInfo_MALFORMED_JSON);  //格式错误
		}
		return return_info;
	}
}
