package com.jcore.Aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Hashtable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.jcore.Frame.ApiCall;
import com.jcore.Web.ApiClient;
import com.jcore.Web.ApiClientConfig;

 

public class Facade implements InvocationHandler {   
	
	public Facade() {
		super();
		 
	}

	
	public Facade(Object objImp) {
		super();
		this.objImp = objImp;
	}



	private Object objImp;
	
    @Override    
    public Object invoke(Object proxy, Method method, Object[] args)    
            throws Throwable { 
    	
    	ApiCall apiCall = method.getAnnotation(ApiCall.class);   
    	if(apiCall==null && objImp !=null)
    	{
    		//System.out.println("11111");
    		return method.invoke(objImp, args);
    	}
    	
    	if(apiCall!=null)
    	{
    		//System.out.println("22222");
        	
        	ApiClient client =	new ApiClient(new ApiClientConfig(apiCall.service()));
        	
        	
        	
        	return client.post(method.getGenericReturnType(), apiCall.method(), args);    
    	}
    	
    	return null;
    	
          
    }    
      
    public static <T> T getService(Class<T> mapperInterface) {  
    	
    	
    	String key = mapperInterface.getName();
    	if(dict.containsKey(key))
    	{
    		return (T)dict.get(key);
    	}
    	
       
        
        lock.lock();
        try {
        	
        	Object tag=null;
        	
        	String impName = getImpName(mapperInterface.getName());
        	
        	Class<?> cla=null;
        	
        	try
        	{
        		cla= Class.forName(impName);  
        		
        	}catch (Exception e) {
        		cla=null;
			}
        	
        	
        	

        	
        	if(cla==null)
        	{
        		 ClassLoader classLoader = mapperInterface.getClassLoader();  
                 Class<?>[] interfaces = new Class[]{mapperInterface};  
                 Facade proxy = new Facade();  
                 tag= (T) Proxy.newProxyInstance(classLoader, interfaces, proxy); 
                 
                 dict.put(key, tag);
        	}
        	else
        	{
        		
        		ClassLoader classLoader = mapperInterface.getClassLoader();  
                Class<?>[] interfaces = new Class[]{mapperInterface};  
                Facade proxy = new Facade(cla.newInstance());  
                tag= (T) Proxy.newProxyInstance(classLoader, interfaces, proxy); 
        		
        		dict.put(key, tag);
        	}
        	 
             return  (T)tag;
             
			
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
        finally {
        	lock.unlock();
		}
             
        
      }  
    
    
    static String getImpName(String interfaceName)
    {
    	String[] strs = interfaceName.split("\\.");
    	
    	
    	String result ="";
    	for(int i=0;i<strs.length;i++)
    	{
    		
    		if(i==strs.length-1)
    		{
    			result = result + "Imp."+ strs[i].substring(1, strs[i].length())+"Imp";   	    	
    		}
    		else
    		{
    			result = result+strs[i]+".";
    		}
    	}
    	return result;
    }
    
    
    static Hashtable<String, Object> dict = new Hashtable<String, Object>();
    
    
    static Lock lock= new ReentrantLock();
    
       
    
    
} 