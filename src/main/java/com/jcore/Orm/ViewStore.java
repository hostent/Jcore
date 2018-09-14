package com.jcore.Orm;

import java.util.HashMap;
import java.util.Map;

public abstract class ViewStore {
	
	Map<String,Object> map = new HashMap<String, Object>();
	
	public void fill(Map<String,Object> map)
	{
		this.map = map;
	}
	
	public Object get(String key)
	{
		return map.get(key);
	}
	
	public void set(String key,Object value)
	{
		map.put(key, value);
	}

}
