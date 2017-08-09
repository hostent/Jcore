package com.jcore.Mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bson.*;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.jcore.Frame.IQuery;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;

public class Query<T> implements IQuery<T>, IMongoQuery<T> {

	public Query(Class<T> classType, String connKey) {
		ClassType = classType;
		ConnKey = connKey;

		MongoDatabase db = ConnectionManager.getDb(ConnKey);
		coll = db.getCollection(ClassType.getSimpleName(), Document.class);

	}

	MongoCollection<Document> coll = null;

	Bson where = new BasicDBObject();

	Bson orderBy = null;

	private String ConnKey;

	private Class<T> ClassType;

	int Form = 0;
	int Length = 0;

	@Override
	public T Get(Object id) {

		MongoDatabase db = ConnectionManager.getDb(ConnKey);
		MongoCollection<Document> col = db.getCollection(ClassType.getSimpleName(), Document.class);

		BsonDocument filter = new BsonDocument();

		BsonObjectId obj = new BsonObjectId(new ObjectId(id.toString()));

		filter.put("_id", obj);

		Document doc = col.find(filter).first();

		T t = null;

		try {
			t = ClassType.newInstance();
			((IEntity) t).fill(doc);

		} catch (InstantiationException | IllegalAccessException e) {

			e.printStackTrace();
		}

		return t;
	}

	@Override
	public T GetUnique(Object unique) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public IMongoQuery<T> Where(Bson filter) {

		where = filter;

		return this;
	}

	@Override
	public IMongoQuery<T> OrderBy(String exp) {

		orderBy = new BasicDBObject(exp, 1);

		return this;
	}

	@Override
	public IMongoQuery<T> OrderByDesc(String exp) {
		orderBy = new BasicDBObject(exp, -1);
		return this;
	}

	@Override
	public IMongoQuery<T> Limit(int form, int length) {
		Form = form;
		Length = length;
		return this;
	}

	@Override
	public T First() {

		FindIterable<Document> finder = coll.find(where);
		if (orderBy != null) {
			finder = finder.sort(orderBy);
		}

		Document doc = finder.first();

		T t = null;
		try {

			t = ClassType.newInstance();

			((IEntity) t).fill(doc);

		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return t;
	}

	@Override
	public List<T> ToList() {

		List<T> resultList = new ArrayList<T>();

		FindIterable<Document> finder = coll.find(where);
		if (orderBy != null) {
			finder = finder.sort(orderBy);
		}

		if (Length != 0) {
			finder = finder.skip(Form).limit(Length);
		}
		MongoCursor<Document> cur = finder.iterator();
		while (cur.hasNext()) {
			T t = null;
			try {
				t = ClassType.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			((IEntity) t).fill(cur.next());
			resultList.add(t);
		}

		return resultList;
	}

	@Override
	public long Count() {
		
		long resultCount =0;
		
		FindIterable<Document> finder = coll.find(where);
		MongoCursor<Document> cur = finder.iterator();
		
		while (cur.hasNext()) {
			resultCount++;
		}
		return resultCount;
	}
	

	@Override
	public boolean Exist() {
		 
		FindIterable<Document> finder = coll.find(where);
		MongoCursor<Document> cur = finder.iterator();
		
		return cur.hasNext();
	}

}
