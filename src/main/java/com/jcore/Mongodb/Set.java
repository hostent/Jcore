package com.jcore.Mongodb;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.bson.*;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import com.jcore.Frame.*;

import com.mongodb.client.*;

import com.mongodb.client.result.*;

public abstract class Set<T> implements ISet<T>, IMongoQuery<T> {

	private String ConnKey;

	public abstract Class<T> getType();

	Query<T> query;

	public Set(String connKey) {
		ConnKey = connKey;
		query = new Query<>(getType(), ConnKey);
	}

	@Override
	public T Get(Object id) {

		return query.Get(id);

	}

	@Override
	public T GetUnique(Object unique) {

		return query.GetUnique(unique);
	}

	@Override
	public int Delete(Object id) {
		MongoDatabase db = ConnectionManager.getDb(ConnKey);
		MongoCollection<Document> col = db.getCollection(getType().getSimpleName(), Document.class);

		BsonDocument deleteCondition = new BsonDocument();

		BsonObjectId obj = new BsonObjectId(new ObjectId(id.toString()));

		deleteCondition.put("_id", obj);

		DeleteResult result = col.deleteOne(deleteCondition);
		return (int) result.getDeletedCount();
	}

	@Override
	public int Update(T t) {

		MongoDatabase db = ConnectionManager.getDb(ConnKey);

		MongoCollection<Document> col = db.getCollection(getType().getSimpleName(), Document.class);

		ICollection docEntity = (ICollection) t;
		docEntity.setCreatetTime(new Date());

		BsonDocument updateCondition = new BsonDocument();

		BsonObjectId objId = new BsonObjectId(new ObjectId(docEntity.getId().toString()));
		updateCondition.put("_id", objId);

		Document upDoc = docEntity.getDoc();
		upDoc.remove("_id");

		Document update = new Document();
		update.append("$set", upDoc);

		UpdateResult result = col.updateOne(updateCondition, update);

		return (int) result.getModifiedCount();
	}

	@Override
	public Object Add(T t) {

		Object objId = ObjectId.get();
		ICollection doc = (ICollection) t;
		doc.setId(objId);
		doc.setCreatetTime(new Date());

		MongoDatabase db = ConnectionManager.getDb(ConnKey);

		MongoCollection<Document> col = db.getCollection(getType().getSimpleName(), Document.class);

		col.insertOne(doc.getDoc());

		return objId;
	}

	@Override
	public List<T> QueryXml(String reportName, Hashtable<String, Object> par) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public PageData<T> QueryXml(String reportName, PagePars param) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void ExecXml(String reportName, Dictionary<String, Object> par) {
		// TODO 自动生成的方法存根

	}

	@Override
	public IMongoQuery<T> Where(Bson filter) {

		return query.Where(filter);
	}

	@Override
	public IMongoQuery<T> OrderBy(String exp) {

		return query.OrderBy(exp);
	}

	@Override
	public IMongoQuery<T> OrderByDesc(String exp) {

		return query.OrderByDesc(exp);
	}

	@Override
	public IMongoQuery<T> Limit(int form, int length) {

		return query.Limit(form, length);
	}

	@Override
	public T First() {

		return query.First();
	}

	@Override
	public List<T> ToList() {

		return query.ToList();
	}

	@Override
	public long Count() {

		return query.Count();
	}

	@Override
	public boolean Exist() {

		return query.Exist();
	}

}
