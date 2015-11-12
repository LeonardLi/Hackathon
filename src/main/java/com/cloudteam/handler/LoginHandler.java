package com.cloudteam.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.sf.json.JSONObject;
import com.cloudteam.utils.DButils;

public class LoginHandler extends MyHandler {

	public LoginHandler() {
		// TODO Auto-generated constructor stub
		super();
	}// ��¼

	public String LoginHand(String login_info) throws SQLException {
		String return_info = null;
		String token = super.getCharAndNumr(20);
		System.out.println(token);
		// ����
		try {
			JSONObject jsonobj = new JSONObject();
			jsonobj = JSONObject.fromObject(login_info);
			String username = jsonobj.getString("username");
			String password = jsonobj.getString("password");
			System.out.println("username" + username);
			System.out.println("password" + password);
			Connection connection = DButils.getConnection(true);
			try {
				StringBuilder sql = new StringBuilder();
				sql.append("select * from users where name='").append(username)
						.append("' && password='").append(password).append("'");
				Statement statement = connection.createStatement();
				ResultSet resultSet = (ResultSet) statement.executeQuery(sql
						.toString());
				if (resultSet.next()) {
					return_info = "200 OK\n{\n" + "\"user_id\": "
							+ resultSet.getString("id") + ",\n\"username\": \""
							+ resultSet.getString("name")
							+ "\",\n\"access_token\": \"" + token + "\"\n}";
					// �����û�Token
					resultSet.absolute(1); // ת����һ�У�����Ψһ��һ����¼
					resultSet.updateString("access_token", token); // ����token
					resultSet.updateRow(); // ����Դ��
				} else
					return super.ErrorCode_USER_AUTH_FAIL; // �û������������
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DButils.close(connection);
			}

		} catch (Exception e) { // ����JSON���ݣ�ֱ�ӷ��ش���
			return super.ErrorCode_MALFORMED_JSON; // �����ʽ����
		}
		return return_info;
	}
}
