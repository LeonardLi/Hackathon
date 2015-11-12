package com.cloudteam.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.sf.json.JSONObject;

public class AddFoodHandler extends MyHandler {

	public AddFoodHandler() {
		// TODO Auto-generated constructor stub
	}

	public String AddFoodHand(String food_info, String access_token,
			String cart_id) throws SQLException {
		String return_info = null;
		String token = super.getCharAndNumr(20);
		// ����
		/*
		 * { "food_id": 2, "count": 1 }
		 */
		/*
		 * try{ JSONObject jsonobj = new JSONObject(); jsonobj =
		 * JSONObject.fromObject(food_info); String food_id =
		 * jsonobj.getString("food_id"); String count =
		 * jsonobj.getString("count"); Connection connection =
		 * DButils.getConnection(true); try { StringBuilder sql = new
		 * StringBuilder();
		 * sql.append("select * from  carts where id=").append(cart_id);
		 * Statement statement = connection.createStatement(); ResultSet
		 * resultSet = (ResultSet) statement.executeQuery(sql.toString()); if
		 * (resultSet.next()) { //���Ӵ���
		 * if(resultSet.getString("access_token").equals(access_token))
		 * //��Ȩ���� { if(Integer.parseInt(resultSet.getString("count")) < 3) //
		 * 
		 * } return_info = "200 OK\n{\n" + "\"user_id\": " +
		 * resultSet.getString("id") + ",\n\"username\": \"" +
		 * resultSet.getString("name") + "\",\n\"access_token\": \""+ token
		 * +"\"\n}"; else return super.ErrorCode_NOT_AUTHORIZED_TO_ACCESS_CART;
		 * }else return super.ErrorCode_USER_AUTH_FAIL; //�û������������ } catch
		 * (SQLException e) { e.printStackTrace(); } finally {
		 * DButils.close(connection); }
		 * 
		 * }catch(Exception e){ //����JSON���ݣ�ֱ�ӷ��ش��� return
		 * super.ErrorCode_MALFORMED_JSON; //�����ʽ���� }
		 */
		return return_info;
	}

}
