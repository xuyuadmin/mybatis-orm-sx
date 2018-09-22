package com.xuyu.orm.aop;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.xuyu.orm.annotation.ExtInsert;
import com.xuyu.orm.annotation.ExtParam;
import com.xuyu.orm.annotation.ExtSelect;
import com.xuyu.orm.utils.JDBCUtils;
import com.xuyu.orm.utils.SQLUtils;

public class MyInvocationHandlerMbatis implements InvocationHandler {
	private Object object;

	public MyInvocationHandlerMbatis(Object object) {
		this.object = object;
	}

	// proxy ������� method���ط��� args�����ϵĲ���ֵ
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("ʹ�ö�̬���������ؽӿڷ�����ʼ");
		// ʹ�ð׻��ʷ���,@ExtInsert��װ����
		// 1. �жϷ������Ƿ����@ExtInsert
		ExtInsert extInsert = method.getDeclaredAnnotation(ExtInsert.class);
		if (extInsert != null) {
			return extInsert(extInsert, proxy, method, args);
		}
		// 2.��ѯ��˼·
		// 1. �жϷ������Ƿ�� ��ע��
		ExtSelect extSelect = method.getDeclaredAnnotation(ExtSelect.class);
		if (extSelect != null) {
			// 2. ��ȡע���ϲ�ѯ��SQL���
			String selectSQL = extSelect.value();
			// 3. ��ȡ�����ϵĲ���,����һ��
			ConcurrentHashMap<Object, Object> paramsMap = paramsMap(proxy, method, args);
			// 4. �����滻�����ݷ�ʽ
			List<String> sqlSelectParameter = SQLUtils.sqlSelectParameter(selectSQL);
			// 5.���ݲ���
			List<Object> sqlParams = new ArrayList<>();
			for (String parameterName : sqlSelectParameter) {
				Object parameterValue = paramsMap.get(parameterName);
				sqlParams.add(parameterValue);
			}
			// 6.��sql����滻��?
			String newSql = SQLUtils.parameQuestion(selectSQL, sqlSelectParameter);
			System.out.println("newSQL:" + newSql + ",sqlParams:" + sqlParams.toString());

			// 5.����jdbc����ײ�ִ��sql���
			// 6.ʹ�÷������ʵ������### ��ȡ�������ص����ͣ�����ʵ����
			// ˼·:
			// 1.ʹ�÷�����ƻ�ȡ����������
			// 2.�ж��Ƿ��н����,����н�������ڽ��г�ʼ��
			// 3.ʹ�÷������,������ֵ

			ResultSet res = JDBCUtils.query(newSql, sqlParams);
			// �ж��Ƿ����ֵ
			if (!res.next()) {
				return null;
			}
			// �±������ƶ�һλ
			res.previous();
			// ʹ�÷�����ƻ�ȡ����������
			Class<?> returnType = method.getReturnType();
			//ʵ��������
			Object object = returnType.newInstance();
			while (res.next()) {
				// ��ȡ��ǰ���е�����
				Field[] declaredFields = returnType.getDeclaredFields();
				for (Field field : declaredFields) {
					//�������name
					String fieldName = field.getName();
					//�������value
					Object fieldValue = res.getObject(fieldName);
					field.setAccessible(true);
					field.set(object, fieldValue);
				}
			}
			return object;
		}

		return null;
	}

	private Object extInsert(ExtInsert extInsert, Object proxy, Method method, Object[] args) {
		// �����ϴ���@ExtInsert,��ȡ����SQL���
		// 2. ��ȡSQL���,��ȡע��Insert���
		String insertSql = extInsert.value();
		// System.out.println("insertSql:" + insertSql);
		// 3. ��ȡ�����Ĳ�����SQL��������ƥ��
		// ��һ��һ��Map���� KEYΪ@ExtParamValue,Value ���Ϊ����ֵ
		ConcurrentHashMap<Object, Object> paramsMap = paramsMap(proxy, method, args);
		// ���sqlִ�еĲ���---�����󶨹���
		String[] sqlInsertParameter = SQLUtils.sqlInsertParameter(insertSql);
		List<Object> sqlParams = sqlParams(sqlInsertParameter, paramsMap);
		// 4. ���ݲ����滻������Ϊ?
		String newSQL = SQLUtils.parameQuestion(insertSql, sqlInsertParameter);
		System.out.println("newSQL:" + newSQL + ",sqlParams:" + sqlParams.toString());
		// 5. ����jdbc�ײ����ִ�����
		return JDBCUtils.insert(newSQL, false, sqlParams);
	}

	private List<Object> sqlParams(String[] sqlInsertParameter, ConcurrentHashMap<Object, Object> paramsMap) {
		List<Object> sqlParams = new ArrayList<>();
		for (String paramName : sqlInsertParameter) {
			Object paramValue = paramsMap.get(paramName);
			sqlParams.add(paramValue);
		}
		return sqlParams;
	}

	private ConcurrentHashMap<Object, Object> paramsMap(Object proxy, Method method, Object[] args) {
		ConcurrentHashMap<Object, Object> paramsMap = new ConcurrentHashMap<>();
		// ��ȡ�����ϵĲ���
		Parameter[] parameters = method.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			ExtParam extParam = parameter.getDeclaredAnnotation(ExtParam.class);
			if (extParam != null) {
				// ��������
				String paramName = extParam.value();
				Object paramValue = args[i];
				// System.out.println(paramName + "," + paramValue);
				paramsMap.put(paramName, paramValue);
			}
		}
		return paramsMap;
	}

	public Object extInsertSQL() {
		return object;
	}
}
