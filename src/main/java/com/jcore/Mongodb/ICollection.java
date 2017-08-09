package com.jcore.Mongodb;

import java.util.Date;

public interface ICollection extends IEntity{
	
	public Object getId(); 
 
	public void setId(Object id);  
	

	public Date getCreatetTime();

 
	public void setCreatetTime(Date createtTime) ;

 
	public Date getUpdateTime() ;

 
	public void setUpdateTime(Date updateTime);

 
	public String getVersion();

 
	public void setVersion(String version) ;
}
