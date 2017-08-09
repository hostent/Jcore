package com.jcore.Mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class ConnectionManager {

	
	public static  MongoDatabase getDb(String connKey)
	{
		@SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient( "127.0.0.1" , 27017 );
		
		MongoDatabase db =mongoClient.getDatabase("Test");
		
		return db;
	}
}
