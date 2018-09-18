package com.jcore.MenberShip;

public interface IUser {

	Integer getId();	
	void setId(Integer id);
	
	String getName();
	void setName(String name);
	
	Boolean getEnable();
	void setEnable(String enable);
	
	String getPassword();
	void setPassword(String password);
	
	Boolean getIsAdmin();
	void setIsAdmin(String isAdmin);
	
	
}
