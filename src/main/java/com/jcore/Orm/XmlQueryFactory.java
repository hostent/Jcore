package com.jcore.Orm;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.jcore.Aop.Facade;
import com.jcore.Frame.IXmlQuery;

 

public class XmlQueryFactory implements InvocationHandler {

	
	public XmlQueryFactory(String connKey) {
		super();
		this.connKey = connKey;
	}

	String connKey="";
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO 自动生成的方法存根
		
		XmlKey xmlKey = method.getAnnotation(XmlKey.class);  
		
		if(xmlKey==null)
		{
			//todo error
			
				
			
		}
		
		QueryImp<?> queryImp = new QueryImp(connKey,method.getReturnType());
		queryImp.reportName=xmlKey.Key();
		queryImp.queryType=(Class<?>)args[0];
		
		return queryImp;	
		
		
	 
	}
	
	 public static <T> T getService(Class<T> mapperInterface,String connKey) {  
		 
		 ClassLoader classLoader = mapperInterface.getClassLoader();  
         Class<?>[] interfaces = new Class[]{mapperInterface};  
         XmlQueryFactory proxy = new XmlQueryFactory(connKey);  
         return (T) Proxy.newProxyInstance(classLoader, interfaces, proxy); 
	 
	 }
	
	

}
