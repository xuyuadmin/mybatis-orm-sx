package com.xuyu.orm.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JdbcTest {

	public static void main(String[] args) throws SQLException {
		// #{  mybatis �滻�ɣ�
//		String insertSql="insert into user(userName,userAge) values(?,?)";
//		ArrayList<Object> arrayList = new ArrayList<>();
//		arrayList.add("����1");
//		arrayList.add(20);
//		
//		int insert = JDBCUtils.insert(insertSql, false, arrayList);
//		System.out.println("insert��"+insert);
		ArrayList<Object> arrayList = new ArrayList<>();
		arrayList.add("����");
		arrayList.add(20);
		//��ѯ���
		ResultSet query = JDBCUtils.query("select * from user where userName=�� and userAge=��", arrayList);
		while (query.next()) {
			int userAge=query.getInt(20);
			String userName=query.getString("userName");
			System.out.println(userAge);
			System.out.println(userName);
		}
	}
	
}
