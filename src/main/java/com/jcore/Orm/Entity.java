package com.jcore.Orm;

import java.lang.reflect.*;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Entity<T> {
	
	public Entity(Class<?> type)
	{
		Type=type;
		
		init();
	}
	
	Class<?> Type=null;
	
	public String tableName="";
	public String key="";
	public String uniqueKey="";
	public Boolean isIdentity=true;
	
	public List<String> columns= new ArrayList<String>();
	
	public void init()
	{
		Table table=  this.getType().getAnnotation(Table.class);
		
		tableName=table.Name();
		key = table.Key();
		uniqueKey = table.UniqueKey();
		
		Identity identity=  this.getType().getAnnotation(Identity.class);
		if(identity==null)
		{
			isIdentity =false;
		}
		
		Method[] methods = this.getType().getDeclaredMethods();

		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			
			JsonProperty jp =method.getAnnotation(JsonProperty.class);
			
			if(jp!=null)
			{
				columns.add(jp.value());
			}
			 
		}
	}

	public Class<?> getType() {		 

		return Type;

	} 
	
	public String[] getColumnSymbol(String[] columns) 
	{
		String[] result = new String[columns.length];
		for (int i = 0; i < columns.length; i++) {		 
			
			result[i]="?";
			
		}
		return result;
	}
	
	public  String getIdValue(T t)
	{
		BaseTable bt = (BaseTable)t;
		return bt.map.get(key).toString();
		 
	}
	
	public Object[] getColumnValues(boolean isIncludeId, T t) {
		
		List<Object> list= new ArrayList<Object>();
		
		BaseTable bt = (BaseTable)t;

		for (int i = 0; i < columns.size(); i++) {
			
			String colName = columns.get(i);

			if ((!isIncludeId) && colName.equals(key)) {
				continue;
			}

			list.add(bt.map.get(colName));
		}

		return  list.toArray();
	}

	public String[]  getColumns(boolean isIncludeId)
	{
		List<String> temp = new ArrayList<String>();
		
		for (String str : columns) {
			if((!isIncludeId) && str.equals(key))
			{
				 continue;
			}
			temp.add(str);
		}
		
		return temp.toArray(new String[temp.size()]);
	}
	
}
