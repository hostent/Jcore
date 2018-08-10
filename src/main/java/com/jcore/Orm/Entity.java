package com.jcore.Orm;

import java.lang.reflect.*;
import java.util.*;

import com.jcore.Frame.*;

public class Entity<T> {
	
	public Entity(Class<?> type)
	{
		Type=type;
	}
	
	Class<?> Type=null;

	public String getKey() {

		Field[] fields = this.getType().getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Key key = field.getAnnotation(Key.class);
			if (key != null) {
				return field.getName();
			}
		}

		return null;
	}

	public boolean hasIdentity() {

		Field[] fields = this.getType().getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Identity identity = field.getAnnotation(Identity.class);
			if (identity != null) {
				return true;
			}
		}

		return false;
	}

	public String getUniqueKey() {

		Field[] fields = this.getType().getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Unique key = field.getAnnotation(Unique.class);
			if (key != null) {
				return field.getName();
			}
		}

		return null;
	}

	public Class<?> getType() {		 

		return Type;

	}

	public String[] getColumns(boolean isIncludeId) {
		ArrayList<String> list = new ArrayList<String>();
		
		Field[] fields = this.getType().getDeclaredFields();
 
		String key = getKey();

		for (int i = 0; i < fields.length; i++) {
			
			Column colum = fields[i].getDeclaredAnnotation(Column.class);
			
			if( colum==null)
			{
				continue;
			}
			
			String colName = colum.Name();
			if ((!isIncludeId) && colName.equals(key)) {
				continue;
			}
			list.add(colName);
		}
		

		return (String[]) list.toArray(new String[list.size()]);
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
		Field[] fields = this.getType().getDeclaredFields();

		String key = getKey();
		Object obj=null;
		
		
		for (int i = 0; i < fields.length; i++) {
			String colName = fields[i].getName();
			if (colName.equals(key)) {
				
				Method method =null;
				Method[] mlist = this.getType().getDeclaredMethods();
				
				for (int j = 0; j < mlist.length; j++) {
					if(mlist[j].getName().toLowerCase().equals(("get" + colName).toLowerCase()))
					{
						method=mlist[j];
						break;
					}
				}
				if(method==null)
				{
					try {
						throw new NoSuchMethodException();
					} catch (NoSuchMethodException e) {
						// TODO 自动生成的 catch 块
						Log.logError(e);
					}
				}

				try {
					obj = method.invoke(t);
				} catch (IllegalAccessException e) {
					// TODO 自动生成的 catch 块
					Log.logError(e);
				} catch (IllegalArgumentException e) {
					// TODO 自动生成的 catch 块
					Log.logError(e);
				} catch (InvocationTargetException e) {
					// TODO 自动生成的 catch 块
					Log.logError(e);
				}
			}
		}
		
		return obj.toString();
  
		 
	}
	
	public Object[] getColumnValues(boolean isIncludeId, T t) {
		ArrayList<Object> list = new ArrayList<Object>();
		Field[] fields = this.getType().getDeclaredFields();

		String key = getKey();
		
	 

		for (int i = 0; i < fields.length; i++) {
			
			Column colum = fields[i].getDeclaredAnnotation(Column.class);
			
			if( colum==null)
			{
				continue;
			}
			
			String colName = fields[i].getName();
			if ((!isIncludeId) && colName.equals(key)) {
				continue;
			}

			Object obj=null;
			try {
				
				Method method =null;
				Method[] mlist = this.getType().getDeclaredMethods();
				
				for (int j = 0; j < mlist.length; j++) {
					if(mlist[j].getName().toLowerCase().equals(("get" + colName).toLowerCase()))
					{
						method=mlist[j];
						break;
					}
				}
				if(method==null)
				{
					throw new NoSuchMethodException();
				}

				obj = method.invoke(t);
				
			} catch (IllegalArgumentException e) {
				Log.logError(e);
			} catch (IllegalAccessException e) {
				Log.logError(e);
			} catch (InvocationTargetException e) {
				Log.logError(e);
			} catch (SecurityException e) {
				Log.logError(e);
			} catch (NoSuchMethodException e) {
				Log.logError(e);
			}

			list.add(obj);
		}

		return  list.toArray();
	}

	
	public String getTableName()
	{
		Table table = this.getType().getDeclaredAnnotation(Table.class);
		
		if(table==null)
		{
			return this.getType().getName();
		}
		
		return table.Name();
		
	}
 
	
}
