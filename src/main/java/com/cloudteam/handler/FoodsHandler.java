package com.cloudteam.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cloudteam.utils.DButils;

/*
 * ��ѯ���
 * */
public class FoodsHandler extends MyHandler {

	public FoodsHandler() {
		// TODO Auto-generated constructor stub
	}

	public String QueryFoodsHand(String access_token) throws SQLException {
		String return_info = null;
		/*try {
			Connection connection = DButils.getConnection(true);
			try {
				StringBuilder sql = new StringBuilder();
				sql.append("select * from users where name='").append("root")
						.append("' && password='").append("toor")
						.append("' && access_token='").append(access_token)
						.append("'");
				Statement statement = connection.createStatement();
				ResultSet resultSet = (ResultSet) statement.executeQuery(sql
						.toString());
				if (resultSet.next()) {
					StringBuilder sql2 = new StringBuilder();
					sql2.append("select * from foods"); // ��������ʳ�����
					resultSet = (ResultSet) statement.executeQuery(sql2
							.toString());
					return_info = "200 OK\n[\n{";
					while (resultSet.next()) {
						return_info += "\"id\": " + resultSet.getInt("id")
								+ ",\"price\": \""
								+ resultSet.getDouble("price") + ",\"stock\":"
								+ resultSet.getInt("stock");
						if (!resultSet.isLast())
							return_info += ",";
					}
					return_info += "}\n]";
				} else
					return "";
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DButils.close(connection);
			}
		} catch (Exception e) { // ����JSON���ݣ�ֱ�ӷ��ش���
			return super.ErrorCode_MALFORMED_JSON; // �����ʽ����
		}*/
		return return_info;
	}
}