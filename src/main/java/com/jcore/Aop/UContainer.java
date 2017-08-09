package com.jcore.Aop;

import com.google.inject.*;
 

public class UContainer {
	
 
	private static  Module DbModuleHandle=null;
	
	private static  Injector DbInjectorHandle=null;
	
	private static  Module ServiceModuleHandle=null;
	
	private static  Injector ServiceInjectorHandle=null;
	
	public static void init(Module dbModuleHandle,Module serviceModuleHandle) throws Exception
	{
		DbModuleHandle=dbModuleHandle;
		ServiceModuleHandle=serviceModuleHandle;
		 
		Bind();
	}
	
	private static void Bind() throws Exception
	{	
		 if(DbModuleHandle==null)
		 {
			 throw new Exception("DbModuleHandle");
		 }
		 
		 if(ServiceModuleHandle==null)
		 {
			 throw new Exception("ServiceModuleHandle");
		 }
		
		 DbInjectorHandle = Guice.createInjector(DbModuleHandle);	
		 
		 ServiceInjectorHandle = Guice.createInjector(ServiceModuleHandle);		
	}
	
	
 
	
	public static <T> T getDb(Class<T> type)
	{
		return DbInjectorHandle.getInstance(type);
				
	}
	
	public static <T> T getService(Class<T> type)
	{
		return ServiceInjectorHandle.getInstance(type);
				
	}
	
}
