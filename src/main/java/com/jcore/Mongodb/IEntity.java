package com.jcore.Mongodb;

 

import org.bson.Document;

public interface IEntity {
	
	Document  getDoc();
	
	void fill(Document _doc);
	
	
	
}
