package com.cloudteam.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.sf.json.JSONObject;
import com.cloudteam.utils.DButils;
import com.cloudteam.utils.*;

public class LoginHandler extends MyHandler {

	public LoginHandler() {
		// TODO Auto-generated constructor stub
		super();
	}// ��¼

	public String LoginHand(String login_info) throws SQLException {
		String return_info = null;
		// ����
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
					return_info = "200 OK\n{\n" + "\"user_id\": "
							+ resultSet.getString("id") + ",\n\"username\": \""
							+ resultSet.getString("name")
							+ "\",\n\"access_token\": \"" + TokenGenerator.getInstance().TokenMap.get(Integer.parseInt(resultSet.getString("id"))) + "\"\n}";
					} else
					return super.ErrorCode_USER_AUTH_FAIL; 
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DButils.close(connection);
			}

		} catch (Exception e) { 
			return super.ErrorCode_MALFORMED_JSON; 
		}
		return return_info;
	}
}
