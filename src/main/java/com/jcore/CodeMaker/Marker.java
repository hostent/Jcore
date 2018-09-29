package com.jcore.CodeMaker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jcore.Web.TemplateEngine;

public class Marker {
	
	public static String getCode(String className) throws ClassNotFoundException
	{
		Class<?> cls = Class.forName(className);
		
		Entity entity = new Entity();
		
		entity.setName(cls.getSimpleName());
		
		List<Column> cols =entity.getCols();
		if(cols==null)
		{
			cols = new ArrayList<Column>();
		}
		
		for (Field field : cls.getDeclaredFields()) {
						
			Column col = new Column();
			col.setColName(field.getName());
			col.setColType(field.getType().getSimpleName());
			
			cols.add(col);
								
		}
		
		entity.setCols(cols);
		
		HashMap<String, Object> dict = new HashMap<String, Object>();
		dict.put("entity", entity);
		dict.put("test", "3332");
		
		String code = TemplateEngine.raw("templates/entity", dict);
		
		return code;
		
		
	}

}
