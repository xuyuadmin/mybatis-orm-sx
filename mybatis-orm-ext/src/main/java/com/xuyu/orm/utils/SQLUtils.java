package com.xuyu.orm.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLƴ��<br>
 */
public class SQLUtils {
	/**
	 * 
	 * ��ȡInsert������values ������Ϣ<br>
	 * @param sql
	 * @return
	 */
	public static String[] sqlInsertParameter(String sql) {
		int startIndex = sql.indexOf("values");
		int endIndex = sql.length();
		String substring = sql.substring(startIndex + 6, endIndex).replace("(", "").replace(")", "").replace("#{", "")
				.replace("}", "");
		String[] split = substring.split(",");
		return split;
	}

	/**
	 * 
	 * ��ȡselect ����where��� ����: ÿ�ؽ���-��ʤ��<br>
	 * @param sql
	 * @return
	 */
	public static List<String> sqlSelectParameter(String sql) {
		int startIndex = sql.indexOf("where");
		int endIndex = sql.length();
		String substring = sql.substring(startIndex + 5, endIndex);
		String[] split = substring.split("and");
		List<String> listArr = new ArrayList<>();
		for (String string : split) {
			String[] sp2 = string.split("=");
			listArr.add(sp2[0].trim());
		}
		return listArr;
	}

	/**
	 * ��SQL���Ĳ����滻��Ϊ?<br>
	 * @param sql
	 * @param parameterName
	 * @return
	 */
	public static String parameQuestion(String sql, String[] parameterName) {
		for (int i = 0; i < parameterName.length; i++) {
			String string = parameterName[i];
			sql = sql.replace("#{" + string + "}", "?");
		}
		return sql;
	}

	public static String parameQuestion(String sql, List<String> parameterName) {
		for (int i = 0; i < parameterName.size(); i++) {
			String string = parameterName.get(i);
			sql = sql.replace("#{" + string + "}", "?");
		}
		return sql;
	}

	public static void main(String[] args) {

		String sql="insert into user(userName,userAge)values(#{userName},#{userAge})";
		 String[] sqlParameter = sqlInsertParameter(sql);
		 for (String string : sqlParameter) {
		 System.out.println(string);
		 }

	}
}
