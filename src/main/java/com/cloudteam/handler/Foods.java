package com.cloudteam.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.cloudteam.utils.DButils;

public class Foods {

	public Foods() {
		// TODO Auto-generated constructor stub
	}

	public String QueryFoodsHand() {
		String return_info = null;
		Connection connection = null;
		JSONObject obj = new JSONObject();
		JSONArray result = new JSONArray();
		try {
			connection = DButils.getConnection(true);

			StringBuilder sql = new StringBuilder();
			Statement statement = connection.createStatement();
			sql.append("select * from food");
			ResultSet resultSet = statement.executeQuery(sql
					.toString());
			if (resultSet.next()) {
				resultSet = statement.executeQuery(sql.toString());
				while (resultSet.next()) {
					
					return_info = "{\"id\": " + resultSet.getString("id")
							+ ",\"price\": " + resultSet.getDouble("price")
							+ ",\"stock\":" + resultSet.getInt("stock")
							+ "}";
					obj = JSONObject.fromObject(return_info);
					result.add(obj);
					
				}
				
				//System.out.println(result);
			} else
				return "";
		} catch (SQLException e) {
			e.printStackTrace();
		}catch(JSONException e){
			e.printStackTrace();
			
		} 
		finally {
			try {
				DButils.close(connection);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result.toString();
	}
}