package com.jcore.Orm;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;

import com.fasterxml.jackson.datatype.jsr310.deser.key.LocalDateTimeKeyDeserializer;
import com.jcore.Frame.Log;
import com.jcore.Tool.DataConverter;




public class RecordMap {

	@SuppressWarnings("unchecked")
	public static <T> T toEntity(Class<?> clazz, ResultSet rs) {
		ResultSetMetaData rsmd = null;
		String temp = "";
		T t = null;
		try {
			rsmd = rs.getMetaData();
			if ((!rs.isLast() )&&rs.next()) {
				t = (T) clazz.newInstance();
				Method[] methods = clazz.getDeclaredMethods();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					temp = rsmd.getColumnName(i);
					
					String methodName ="set"+temp;
					Method method=null;
					for (int j = 0; j < methods.length; j++) {
						Method methodTemp = methods[j];
						if(methodTemp.getName().toLowerCase().equals(methodName.toLowerCase()))
						{
							method =methodTemp;
						}
					}	
					if(method==null)
					{
						throw new NoSuchMethodException();
					}
					
					Type paramType = method.getGenericParameterTypes()[0];
					Object dataTag =DataConverter.parse(paramType, rs.getObject(temp));
					
					method.invoke(t, dataTag);
				}
			}
			if(!rs.isClosed())
			{
				rs.close();
			}			 
		} catch (SQLException e) {
			Log.logError(e,"字段："+temp);
		} catch (IllegalArgumentException e) {
			Log.logError(e,"字段："+temp);
		} catch (IllegalAccessException e) {
			Log.logError(e,"字段："+temp);
		} catch (InvocationTargetException e) {
			Log.logError(e,"字段："+temp);
		} catch (SecurityException e) {
			Log.logError(e,"字段："+temp);
		} catch (NoSuchMethodException e) {
			Log.logError(e,"字段："+temp);
		} catch (InstantiationException e) {
			Log.logError(e,"字段："+temp);
		}
		return t;
	}
	

	@SuppressWarnings("unchecked")
	public static <T> List<T> toList(Class<?> clazz, ResultSet rs)
	{
		List<T> list = new ArrayList<T>();
				
		ResultSetMetaData rsmd = null;
		String temp = "";
		T t = null;
		try {
			rsmd = rs.getMetaData();
			Method[] methods = clazz.getMethods();
			while (rs.next()) {
				t = (T) clazz.newInstance();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					temp = rsmd.getColumnName(i);
					String methodName ="set"+temp;
					Method method=null;
					for (int j = 0; j < methods.length; j++) {
						Method methodTemp = methods[j];
						if(methodTemp.getName().toLowerCase().equals(methodName.toLowerCase()))
						{
							method =methodTemp;
						}
					}	
					if(method==null)
					{
						throw new NoSuchMethodException();
					}
					Type paramType = method.getGenericParameterTypes()[0];	
					
					Object dataTag =DataConverter.parse(paramType, rs.getObject(temp));
					
					method.invoke(t, dataTag);
					
				}
				
				list.add(t);
			}			
			if(!rs.isClosed())
			{
				rs.close();
			}
		} catch (SQLException e) {
			Log.logError(e,"字段："+temp);
		} catch (IllegalArgumentException e) {
			Log.logError(e,"字段："+temp);
		} catch (IllegalAccessException e) {
			Log.logError(e,"字段："+temp);
		} catch (InvocationTargetException e) {
			Log.logError(e,"字段："+temp);
		} catch (SecurityException e) {
			Log.logError(e,"字段："+temp);
		} catch (InstantiationException e) {
			Log.logError(e,"字段："+temp);
		} catch (NoSuchMethodException e) {
			Log.logError(e,"字段："+temp);
		}
		return list;
	}
	


	
}
