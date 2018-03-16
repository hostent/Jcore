package com.jcore.Web;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Hashtable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.jcore.Frame.ApiCall;
 
/** 
 *  JDK动态代理代理类  
 * 
 */  
@SuppressWarnings("unchecked")  
public class ApiFacadeProxy implements InvocationHandler {    
	
    @Override    
    public Object invoke(Object proxy, Method method, Object[] args)    
            throws Throwable {    

    	ApiCall apiCall = method.getAnnotation(ApiCall.class);   	
    	
    	ApiClient client =	new ApiClient(new ApiClientConfig(apiCall.service()));
    	
    	return client.post(method.getReturnType(), apiCall.method(), args);    	
             
    }    
      
    public static <T> T newMapperProxy(Class<T> mapperInterface) {  
    	
    	
    	String key = mapperInterface.getName();
    	if(dict.containsKey(key))
    	{
    		return (T)dict.get(key);
    	}
    	
       
        
        lock.lock();
        try {
        	
        	 ClassLoader classLoader = mapperInterface.getClassLoader();  
             Class<?>[] interfaces = new Class[]{mapperInterface};  
             ApiFacadeProxy proxy = new ApiFacadeProxy();  
             Object tag = (T) Proxy.newProxyInstance(classLoader, interfaces, proxy); 
             
             dict.put(key, tag);
             
             return  (T)tag;
			
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
        finally {
        	lock.unlock();
		}
             
        
      }  
    
    
    
    static Hashtable<String, Object> dict = new Hashtable<String, Object>();
    
    
    static Lock lock= new ReentrantLock();
    
       
    
    
} 