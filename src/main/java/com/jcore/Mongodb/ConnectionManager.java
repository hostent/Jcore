package com.jcore.Mongodb;

import java.util.Arrays;

import com.jcore.Tool.PropertiesHelp;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class ConnectionManager {
	
	
	
	static String userName =PropertiesHelp.get("log_mongodb", "userName");
	static String password =PropertiesHelp.get("log_mongodb", "password");
	static String address =PropertiesHelp.get("log_mongodb", "address");
	static String port =PropertiesHelp.get("log_mongodb", "port");

	
	public static  MongoDatabase getDb(String connKey)
	{
			
		
		MongoCredential credential = MongoCredential.createCredential(userName, "admin", password.toCharArray());
		MongoClient mongoClient = new MongoClient(new ServerAddress(address, Integer.parseInt(port)), Arrays.asList(credential));
		
		
		MongoDatabase db =mongoClient.getDatabase(connKey);
		
		return db;
	}
}
