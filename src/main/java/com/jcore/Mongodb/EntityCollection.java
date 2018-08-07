package com.jcore.Mongodb;

import java.util.Date;

import org.bson.Document;

public abstract class EntityCollection<T extends IEntity>  {

	
	protected abstract T get();
	
	T t=null;
	
	T getT()
	{
		if(t==null)
		{
			t= get();
		}
		return t;
	}

	public Object getId() {
		return getDoc().get("_id");
	}
 
	public void setId(Object id) {
		getDoc().put("_id", id);
	}
	
 
	public Date getCreatetTime() {
		return getDoc().getDate("CreatetTime");
	}

 
	public void setCreatetTime(Date createtTime) {
		getDoc().put("CreatetTime", createtTime);
	}

 
	public Date getUpdateTime() {
		return getDoc().getDate("UpdateTime");
	}

 
	public void setUpdateTime(Date updateTime) {
		getDoc().put("UpdateTime", updateTime);
	}

 
	public String getVersion() {
		return getDoc().getString("Version");
	}

 
	public void setVersion(String version) {
		getDoc().put("Version", version);
	}


	public Document getDoc() {			
		 
		return getT().getDoc();
	}
 
	public void fill(Document _doc) {
		getT().fill(_doc);
		
	}
	
	
}
