package com.jcore.Orm;

import com.jcore.Frame.IXmlQuery;

 

public class QueryImp<T> extends DbSetMy<T> {

	
	//static final String connKey="wykjt";

	public QueryImp(String connKey, Class<T> type) {
		super(connKey,type);
		 
	}

	@Override
	public Class<?> getType() {
		// TODO 自动生成的方法存根
		return queryType;
	}

	Class<?> queryType;

	 
	
	
//	public static <T> IXmlQuery<T> getQuery(String connKey,String reportName,Class<T> type)
//	{
//		QueryFactory<T> query = new QueryFactory<T>(connKey,type);
//		
//		query.reportName=reportName;
//		
//		return query;
//	}

}
