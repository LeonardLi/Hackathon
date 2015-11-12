package com.cloudteam.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cloudteam.utils.DButils;

public class CreateCartsHandler extends MyHandler {

	public CreateCartsHandler() {
		// TODO Auto-generated constructor stub
	}

	public String CreateCartsHand(String access_token) throws SQLException {
		String return_info = null;
		// ����
		try {
			Connection connection = DButils.getConnection(true);
			try {
				String sql = "insert into carts(access_token) values(?)";
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setString(1, access_token);
				ps.executeUpdate();
				connection.commit();
				// ����ID
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					return_info = rs.getLong(1) + "";
					;
				}
			} catch (SQLException e) {
				connection.rollback();
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
