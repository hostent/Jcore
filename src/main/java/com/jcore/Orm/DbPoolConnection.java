package com.jcore.Orm;

import com.alibaba.druid.pool.*;
import com.jcore.Frame.Log;
import com.jcore.Tool.PropertiesHelp;

import java.util.HashMap;
import java.util.Properties;
import java.sql.SQLException;
import java.io.*;
 

public class DbPoolConnection {
	private static DbPoolConnection databasePool = null;
	
	private static HashMap<String, DruidDataSource> dds = null;
	
	
	private DbPoolConnection() {}
	public static synchronized DbPoolConnection getInstance() {
		if (null == databasePool || dds ==null) {
			databasePool = new DbPoolConnection();
			dds=new HashMap<String, DruidDataSource>();
		}
		return databasePool;
	}
	public DruidPooledConnection getConnection(String connKey) throws SQLException {
		DruidDataSource ddsItem=null;
		if(!dds.containsKey(connKey))
		{
			Properties properties = loadPropertyFile("db_"+connKey+".properties");			
			try {
				ddsItem = (DruidDataSource)DruidDataSourceFactory.createDataSource(properties);
				
				synchronized (databasePool) {
					dds.put(connKey,ddsItem);
				} 
			} catch (Exception e) {
				Log.logError(e);
			}
		}
		else
		{
			ddsItem =dds.get(connKey);
		}		
		return ddsItem.getConnection();
	}
	
	public static Properties loadPropertyFile(String fullFile) {
		 						
		Properties pro = new Properties();					
					
		try {
			pro.load(PropertiesHelp.class.getClassLoader().getResourceAsStream(fullFile));
		} catch (IOException e) {
			Log.logError(e);
		}
		System.out.println("xxxxxxx:fullFile:("+ fullFile  +")xxxxxxxxxxxxxxxxxxxxxx");
		System.out.println("xxxxxxx:url:("+pro.getProperty("url") +")xxxxxxxxxxxxxxxxxxxxxx");
		System.out.println("xxxxxxx:username:("+pro.getProperty("username") +")xxxxxxxxxxxxxxxxxxxxxx");
					
		return pro;	 
		 
	}
	

	
//	public long beginTran()  {
//		return dds.createTransactionId();
//	}
	
//	public long commitTran() 
//	{
//		return dds.gett
//	}
	
}