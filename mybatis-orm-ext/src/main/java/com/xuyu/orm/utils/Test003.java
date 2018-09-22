package com.xuyu.orm.utils;

import com.xuyu.orm.mapper.UserMapper;
import com.xuyu.orm.sqlSession.SqlSession;

/**
 * 使用动态代理方式虚拟调用方法
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
