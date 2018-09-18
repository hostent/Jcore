package com.jcore.Web;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Dictionary;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class  CloudClient{

	static final MediaType JSON_med = MediaType.parse("application/json;charset=utf-8");
	ClientConfig _config;
	
	public CloudClient(ClientConfig config)
	{
		_config = config;
	}
	
	private String getUrl(String methods)
	{
		return "http://"+_config.get_url()+methods;
	}
	
	public Object post(Type returnType,String methods,  Object[] args) throws IOException {
		OkHttpClient httpClient = new OkHttpClient();


		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(args);

		RequestBody requestBody = RequestBody.create(JSON_med, json);		 
				
		okhttp3.Request request = new okhttp3.Request.Builder().url(getUrl(methods)).post(requestBody).build();

		okhttp3.Response response = httpClient.newCall(request).execute();

		String str = response.body().string();

		return JSON.parseObject(str, returnType,null);
		 

	}
}
