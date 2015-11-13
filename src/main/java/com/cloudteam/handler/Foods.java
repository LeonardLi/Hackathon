package com.cloudteam.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cloudteam.hackathonServer.newServer;
import com.cloudteam.utils.DButils;

public class Foods {

	public Foods() {
		// TODO Auto-generated constructor stub
	}

	public String QueryFoodsHand() {
		String return_info = null;
		Connection connection = null;
		try {
			connection = DButils.getConnection(true);

			StringBuilder sql = new StringBuilder();
			Statement statement = connection.createStatement();
			sql.append("select * from food");
			ResultSet resultSet = (ResultSet) statement.executeQuery(sql
					.toString());
			if (resultSet.next()) {
				resultSet = (ResultSet) statement.executeQuery(sql.toString());
				return_info = "[";
				while (resultSet.next()) {
					return_info += "{\"id\": " + resultSet.getInt("id")
							+ ",\"price\": \"" + resultSet.getDouble("price")
							+ "\",\"stock\":\"" + resultSet.getInt("stock")
							+ "\"}";
					if (!resultSet.isLast())
						return_info += ",";
				}
				return_info += "]";
				System.out.println(return_info);
			} else
				return "";
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				DButils.close(connection);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return return_info;
	}
}