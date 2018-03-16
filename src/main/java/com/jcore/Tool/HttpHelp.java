package com.jcore.Tool;

import java.io.IOException;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.jcore.Frame.JFrameException;

import okhttp3.*;

public class HttpHelp {

	public static final MediaType Form = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
	
	public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

	public static String post(String url, HashMap<String, Object> head, HashMap<String, Object> body) throws JFrameException {

		FormBody.Builder build = new FormBody.Builder();

		for (Entry<String, Object> entry : body.entrySet()) {

			build.add(entry.getKey(), entry.getValue().toString());
		}

		RequestBody requestBody = build.build();

		Request.Builder reqBuild = new Request.Builder();
		
		reqBuild.addHeader("content-type", "application/x-www-form-urlencoded;charset:utf-8");

		if (head != null && (!head.isEmpty())) {
			for (Entry<String, Object> entry : head.entrySet()) {
				reqBuild.header(entry.getKey(), entry.getValue().toString());
			}
		}

		Request requestPost = new Request.Builder().url(url).post(requestBody).build();

		OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
				.readTimeout(10, TimeUnit.SECONDS).build();

		okhttp3.Response response;
		String str="";
		try {
			response = client.newCall(requestPost).execute();

			str = response.body().string();
			
			return str;
			
		} catch (IOException e) {
			 throw  JFrameException.apiCallError(requestPost.method(), str, e);
		}

		

	}
	
	public static String postJson(String url,String json) {
	    
	    OkHttpClient okHttpClient = new OkHttpClient();
	    
	    RequestBody requestBody = RequestBody.create(JSON, json);
	    
	    Request request = new Request.Builder()
	            .url(url)
	            .post(requestBody)
	            .build();
	    
	    request.newBuilder().addHeader("content-type", "application/json;charset=utf-8");
	     
	    try {
	    Response response=okHttpClient.newCall(request).execute();
	    
	    return response.body().string();
	       
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        return "";
	    }

	}
 

}
