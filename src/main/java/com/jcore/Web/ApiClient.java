package com.jcore.Web;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcore.Frame.Request;
import com.jcore.Frame.Response;
import com.jcore.Tool.Md5Help;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class ApiClient {

	String _url = "";
	String _secret = "";
	String _key = "";

	public ApiClient(String key, String url, String secret) {

		_url = url;
		_key = key;
		_secret = secret;
	}
	
	public ApiClient(ApiClientConfig config)
	{
		_url = config.get_url();
		_key = config.get_key();
		_secret = config.get_secret();
	}

	private String getUrl(String json) {
		_url = _url + "?key={key}&sign={sign}";

		_url = _url.replace("{key}", _key);

		String sign = Md5Help.toMD5(_key + _secret + json);
		
		_url = _url.replace("{sign}", sign);

		return _url;
	}

	public static final MediaType JSON_med = MediaType.parse("application/json;charset=utf-8");

	public <T> T post(Type returnType, String method, Object...params) throws IOException {
		OkHttpClient httpClient = new OkHttpClient();

		Request req = new Request();
		UUID uuid = UUID.randomUUID();
		req.setId(uuid.toString());
		req.setMethod(method);
		req.setParams(params);

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(req);

		RequestBody requestBody = RequestBody.create(JSON_med, json);
		okhttp3.Request request = new okhttp3.Request.Builder().url(getUrl(json)).post(requestBody).build();

		okhttp3.Response response = httpClient.newCall(request).execute();

		String str = response.body().string();

		Response resp = objectMapper.readValue(str, Response.class);
		if (resp.getError() != null && (!resp.getError().isEmpty())) {
			// log error
			return null;
		}
		if (resp.getResult() != null) {
			
			String tempJson = objectMapper.writeValueAsString(resp.getResult());
			
			T t =null;
			if(returnType.getTypeName().startsWith("java.util.List"))
			{
								 				
				Type[] genericTypes = ((ParameterizedType)returnType).getActualTypeArguments();
			    Type genericType =genericTypes[0];
			    
			   int size = ((List) objectMapper.readValue(tempJson, List.class)).size();

			   
			   Type[] types = new Type[size];
			    for (int j = 0; j < types.length; j++) {
			    	types[j]=genericType;
				}

			    
				t= (T)JSON.parseArray(tempJson, types);
				
				//TypeReference<T> tt = new TypeReference<T>(){};
				
				//JSON.p
				
			// T temp =	JSON.parseObject(tempJson, new TypeReference<T>(){});
				
				//JSON.p
			 
			 //System.out.println(temp.toString());
				
			}
			else
			{
				t = (T)JSON.parseObject(tempJson, returnType, null);
				
				
				
				//t = (T)objectMapper.readValue(tempJson, returnType.getClass());
			}

			return t;
		}

		return null;

	}

}
