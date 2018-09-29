package com.jcore.Web;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcore.Frame.Log;
import com.jcore.Frame.Response;

 

//@WebFilter(filterName = "RestApiFilter", urlPatterns = "/api/*")
public class CloudApiFilter implements Filter {

	String pathRoot ="";
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("xxxxxxxxxxxxxxx:init");
		
		pathRoot = this.getClass().getAnnotation(WebFilter.class).urlPatterns()[0].replace("*", "");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		System.out.println("xxxxxxxxxxxxxxx:doFilter");
		Response apiResponse = new Response();
		
		String json = getJson(request);
		if(json==null || json.isEmpty())
		{
			apiResponse.setError("-90:传输数据错误,Json空");
			outPutResponse(response, apiResponse);
			return;
		}
 
		//log
		Log.apiLog.info(json);
		
		String method = ((HttpServletRequest)request).getRequestURI().replace(pathRoot, "");
		
		ApiBeanStore apiStore = FillApiStore(method);

		if (apiStore == null) {
			apiResponse.setError("-100:找不到方法");
			outPutResponse(response, apiResponse);
			return;
		}
		
		
		Object[] pars = JSON.parseArray(json, apiStore.getMethod().getGenericParameterTypes()).toArray();

		// usercheck
		if (apiStore.getBeanInstance() instanceof IAuth) {
			IAuth authBean = (IAuth) apiStore.getBeanInstance();
			boolean checkResult = authBean.userCheck(method, apiStore.getMethod().getName(), pars);
			if (!checkResult) {
				apiResponse.setError("-102:usercheck fail");
				outPutResponse(response, apiResponse);
				return;
			}
		}

		try {			

			Object obj = apiStore.Invoke(pars);// 执行

			outPutResponse(response, obj);

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			apiResponse.setError("-110:" + e.getMessage());			
			outPutResponse(response, apiResponse);
		}		

	}

	private void outPutResponse(ServletResponse response, Object apiResponse)
			throws JsonProcessingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(apiResponse);
		
		//log
		//Log.apiLog.info(json);

		response.setContentType("application/json;charset=UTF-8");

		try (PrintWriter out = response.getWriter()) {

			out.write(json);

		}

		response.flushBuffer();
	}

	private ApiBeanStore FillApiStore(String method) {

		ApiBeanStore apiStore = null;

		for (ApiBeanStore store : ApiBeanStore.StoreList) {

			if (store.getMethodKey().equals(method.toLowerCase())) {
				apiStore = store;
				continue;
			}

		}
		return apiStore;
	}

 

	private String getJson(ServletRequest request) throws IOException {
		int totalbytes = request.getContentLength();
		if (totalbytes <= 0) {
			return null;
		}
		char[] dataOrigin = new char[totalbytes];

		DataInputStream in = new DataInputStream(request.getInputStream());

		InputStreamReader sr = new InputStreamReader(in, "utf-8");

		sr.read(dataOrigin);
		in.close();

		String reqContent = new String(dataOrigin);
		if (reqContent == null || reqContent.isEmpty()) {
			return null;
		}
		return reqContent;
	}

	@Override
	public void destroy() {
		System.out.println("xxxxxxxxxxxxxxx:destroy");
	}

}
