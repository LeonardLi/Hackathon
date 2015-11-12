package com.cloudteam.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cloudteam.utils.DBHelper;

public class Demo {

	static String sql = null;
	static DBHelper db1 = null;
	static ResultSet ret = null;

	public static void main(String[] args) {
		sql = "select *from food";//SQL语句
		db1 = new DBHelper(sql);//创建DBHelper对象

		try {
			ret = db1.pst.executeQuery();//执行语句，得到结果集
			while (ret.next()) {
				String foodid = ret.getString(1);
				String food_stock = ret.getString(2);
				String food_price = ret.getString(3);
				System.out.println(foodid + "\t" + food_stock + "\t" + food_price  );
			}//显示数据
			ret.close();
			db1.close();//关闭连接
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
