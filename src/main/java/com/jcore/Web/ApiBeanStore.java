package com.jcore.Web;

import java.io.IOException;
import java.lang.reflect.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jcore.Frame.ApiMethod;

public class ApiBeanStore {

	public static List<ApiBeanStore> StoreList = new ArrayList<ApiBeanStore>();

	public static void FillList(Map<String, Object> beans) {

		for (String key : beans.keySet()) {

			Method[] methods = beans.get(key).getClass().getDeclaredMethods();

			for (Method method : methods) {

				ApiMethod apiMethod = method.getAnnotation(ApiMethod.class);
				if(apiMethod==null)
				{
					continue;
				}
				
				ApiBeanStore store = new ApiBeanStore();
				store.setBeanInstance(beans.get(key));
				
				store.setMethod(method);
				store.setMethodKey(apiMethod.value().toLowerCase());
				StoreList.add(store);
			}

			
		}

	}

	
	private Object beanInstance;

	private Method method;

	private String methodKey;

	public Object[] TypeOfArgs(Object[] args) {
		
		//JSON.
		

		//ObjectMapper objectMapper = new ObjectMapper();
		//objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));

		Object[] par = new Object[args.length];

		for (int i = 0; i < args.length; i++) {
			Object object = args[i];

			// k,v 组 = 实体
			if (object.getClass().getName().equals("java.util.LinkedHashMap")) {
								
				String strItem;
				
				strItem = JSON.toJSONString(object);		
				
				Type paramType = method.getGenericParameterTypes()[i];//获取参数泛型
				
				 par[i] = JSON.parseObject(strItem, paramType);		
				 
				 
			} 
			//独立 数组 = 数组
			else if(method.getParameterTypes()[i].getName().equals("java.util.List")){
				int size = ((List)object).size();
				String strItem;
				strItem = JSON.toJSONString(object);					

				
				Type paramType = method.getGenericParameterTypes()[i];//获取参数泛型
				
				Type[] genericTypes = ((ParameterizedType)paramType).getActualTypeArguments();
			    Type genericType =genericTypes[0];
			    
			    Type[] types = new Type[size];
			    for (int j = 0; j < types.length; j++) {
			    	types[j]=genericType;
				}
			    
			    par[i] = JSON.parseArray(strItem, types);			    
			    
			     
			}			
			else {
				par[i] = object;
			}

		}

		return par;
	}
	
    private static Class getClass(Type type, int i) {     
        if (type instanceof ParameterizedType) { // 处理泛型类型     
            return getGenericClass((ParameterizedType) type, i);     
        } else if (type instanceof TypeVariable) {     
            return (Class) getClass(((TypeVariable) type).getBounds()[0], 0); // 处理泛型擦拭对象     
        } else {// class本身也是type，强制转型     
            return (Class) type;     
        }     
    }     
    
    private static Class getGenericClass(ParameterizedType parameterizedType, int i) {     
        Object genericClass = parameterizedType.getActualTypeArguments()[i];     
        if (genericClass instanceof ParameterizedType) { // 处理多级泛型     
            return (Class) ((ParameterizedType) genericClass).getRawType();     
        } else if (genericClass instanceof GenericArrayType) { // 处理数组泛型     
            return (Class) ((GenericArrayType) genericClass).getGenericComponentType();     
        } else if (genericClass instanceof TypeVariable) { // 处理泛型擦拭对象     
            return (Class) getClass(((TypeVariable) genericClass).getBounds()[0], 0);     
        } else {     
            return (Class) genericClass;     
        }     
    }    

	public Object Invoke(Object[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		

			return method.invoke(beanInstance, args);


	}

	public Object getBeanInstance() {
		return beanInstance;
	}

	public void setBeanInstance(Object beanInstance) {
		this.beanInstance = beanInstance;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getMethodKey() {
		return methodKey;
	}

	public void setMethodKey(String methodKey) {
		this.methodKey = methodKey;
	}


	
	
	
}



