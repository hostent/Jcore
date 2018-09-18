package com.jcore.MenberShip;

public interface IGroup {
	
	Integer getId();	
	void setId(Integer id);
	
	String getName();
	void setName(String name);
	
	Boolean getEnable();
	void setEnable(String enable);
	
	String getParent();
	void setParent(String parent);
	
	//机构，岗位
	String getType();
	void setType(String type);
}
