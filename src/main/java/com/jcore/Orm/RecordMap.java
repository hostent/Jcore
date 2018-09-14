package com.jcore.Orm;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import com.jcore.Tool.DataConverter;

public class RecordMap {

	@SuppressWarnings("unchecked")
	public static <T> List<T> toList(Class<?> clazz, ResultSet rs) throws SQLException, NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException {
		List<T> list = new ArrayList<T>();

		ResultSetMetaData rsmd =  rs.getMetaData();;
		rsmd = rs.getMetaData();
 
		while (rs.next()) {

			Map<String, Object> map = new HashMap<String, Object>();
			int count = rsmd.getColumnCount();

			for (int i = 1; i <= count; i++) {
				String key = rsmd.getColumnLabel(i);
				Method method = clazz.getDeclaredMethod("get" + key);
				if(method==null)
				{
					continue;
				}
				Type paramType = method.getGenericReturnType();
				Object value = DataConverter.parse(paramType, rs.getObject(key));
				map.put(key, value);
			}			 

			T t = (T) clazz.newInstance();

			((ViewStore) t).fill(map);

			list.add(t);
		}
		if (!rs.isClosed()) {
			rs.close();
		}

		return list;
	}

	public static <T> T toEntity(Class<?> clazz, ResultSet rs) throws SQLException, NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException {

		Map<String, Object> map = new HashMap<String, Object>();
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		
		if(!rs.next())
		{
			return null;
		}

		for (int i = 1; i <= count; i++) {
			String key = rsmd.getColumnLabel(i);
			Method method = clazz.getDeclaredMethod("get" + key);
			if(method==null)
			{
				continue;
			}
			Type paramType = method.getGenericReturnType();
			Object value = DataConverter.parse(paramType, rs.getObject(key));
			map.put(key, value);
		}

		if (!rs.isClosed()) {
			rs.close();
		}

		@SuppressWarnings("unchecked")
		T t = (T) clazz.newInstance();

		((ViewStore) t).fill(map);

		return t;
	}

}
