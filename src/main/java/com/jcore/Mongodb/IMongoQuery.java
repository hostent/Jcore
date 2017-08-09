package com.jcore.Mongodb;

import java.util.List;

import org.bson.conversions.Bson;
 

public interface IMongoQuery<T> {

	IMongoQuery<T> Where(Bson filter);

	IMongoQuery<T> OrderBy(String exp);

	IMongoQuery<T> OrderByDesc(String exp);

	IMongoQuery<T> Limit(int form, int length);   

    T First();
    
    List<T> ToList();

    long Count();

    boolean Exist();
}
