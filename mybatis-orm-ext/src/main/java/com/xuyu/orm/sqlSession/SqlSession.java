package com.xuyu.orm.sqlSession;

import java.lang.reflect.Proxy;

import com.xuyu.orm.aop.MyInvocationHandlerMbatis;


public class SqlSession {

	//¼ÓÔØmapper½Ó¿Ú
	@SuppressWarnings("unchecked")
	public static  <T> T getMapper(Class classz) {
		return (T) Proxy.newProxyInstance(classz.getClassLoader(), new Class[] {classz}, new MyInvocationHandlerMbatis(classz));
		
	}
}
