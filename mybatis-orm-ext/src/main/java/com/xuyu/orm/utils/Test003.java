package com.xuyu.orm.utils;

import com.xuyu.orm.mapper.UserMapper;
import com.xuyu.orm.sqlSession.SqlSession;

/**
 * ʹ�ö�̬����ʽ������÷���
 * @author Administrator
 *
 */
public class Test003 {

	public static void main(String[] args) {
		UserMapper userMapper = SqlSession.getMapper(UserMapper.class);
		int insertUser = userMapper.insertUser("xuyu4", 142);
		System.out.println("insertUser:"+insertUser);
	}
}
