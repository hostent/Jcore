package com.jcore.Web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
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

		ObjectMapper objectMapper = new ObjectMapper();
		

		Object[] par = new Object[args.length];

		for (int i = 0; i < args.length; i++) {
			Object object = args[i];

			if (object.getClass().getName().equals("java.util.LinkedHashMap")) {
				String strItem;
				try {

					strItem = objectMapper.writeValueAsString(object);
					objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
					par[i] = objectMapper.readValue(strItem, method.getParameterTypes()[i]);

				} catch (IOException e) {

					e.printStackTrace();
				}
			} else {
				par[i] = object;
			}

		}

		return par;
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



