package com.jcore.Tool;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class DataConverter{

	public static <T> T parse(Class<T> paramType, Object dataSource)
	{    
		return parse((Type)paramType,dataSource);
	}
	
	
	public static  <T> T parse(Type paramType, Object dataSource)
	{

		//Type paramType = method.getGenericParameterTypes()[0];
		if(dataSource==null)
		{
			return null;
		}
		
		Object t=dataSource.toString();
		
		//Integer
		if(paramType==Integer.class || paramType==int.class)
		{
			t= Integer.parseInt(dataSource.toString());	

		}
		//Date		
		else if(paramType==Date.class)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				t = sdf.parse(dataSource.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		else if(paramType==LocalDateTime.class)
		{
			t= LocalDateTime.parse(dataSource.toString());
		}
		else if(paramType==LocalDate.class)
		{
			t= LocalDate.parse(dataSource.toString());
		}
		//Decimal
		else if(paramType==BigDecimal.class  )
		{
			t= new BigDecimal(dataSource.toString());			 
		}
		//	Float
		else if(paramType==Float.class || paramType==float.class)
		{
			t= Float.parseFloat(dataSource.toString());	

		}
		//Double
		else if(paramType==Double.class || paramType==double.class)
		{
			t= Double.parseDouble(dataSource.toString());	
		}
		//Boolean
		else if(paramType==Boolean.class || paramType==boolean.class)
		{
			t= Boolean.parseBoolean(dataSource.toString());	
		}
		//Byte
		else if(paramType==Byte.class || paramType==byte.class)
		{
			t= Byte.parseByte(dataSource.toString());	
		}
		//Long
		else if(paramType==Long.class || paramType==Long.class)
		{
			t= Long.parseLong(dataSource.toString());	
		}
		//String
		else if(paramType==String.class) 
		{
			t= dataSource.toString();	
		}
		 
		return (T) t;	
	}
}
