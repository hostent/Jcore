package com.jcore.Web;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcore.Frame.Log;
import com.jcore.Frame.Request;
import com.jcore.Frame.Response;

//@WebFilter(filterName = "RestApiFilter", urlPatterns = "/restapi")
public class RestApiFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("xxxxxxxxxxxxxxx:init");
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
		Request apiReq = FillApiRequest(json);
		

		if (apiReq == null) {
			apiResponse.setError("-90:传输数据错误");
			outPutResponse(response, apiResponse);
			return;
		}
		
		//log
		Log.apiLog.info(json);

		apiResponse.setId(apiReq.getId());

		ApiBeanStore apiStore = FillApiStore(apiReq);

		if (apiStore == null) {
			apiResponse.setError("-100:找不到方法");
			outPutResponse(response, apiResponse);
			return;
		}

		// 这里判断授权
		String key = request.getParameter("key");
		String sign = request.getParameter("sign");
		if (!new Auth(key).checkAuth(sign, json)) {
			apiResponse.setError("-101:sign签名错误");
			outPutResponse(response, apiResponse);
			return;
		}

		apiReq.setParams(apiStore.TypeOfArgs(apiReq.getParams())); // 类型化参数

		// usercheck
		if (apiStore.getBeanInstance() instanceof IAuth) {
			IAuth authBean = (IAuth) apiStore.getBeanInstance();
			boolean checkResult = authBean.userCheck(key, apiStore.getMethod().getName(), apiReq.getParams());
			if (!checkResult) {
				apiResponse.setError("-102:method is forbid，Params and key is not match");
				outPutResponse(response, apiResponse);
				return;
			}
		}

		try {

			Object obj = apiStore.Invoke(apiReq.getParams());// 执行

			apiResponse.setResult(obj);

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

			apiResponse.setError("-110:" + e.getMessage());

			e.printStackTrace();
		}

		outPutResponse(response, apiResponse);

	}

	private void outPutResponse(ServletResponse response, Response apiResponse)
			throws JsonProcessingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(apiResponse);
		
		//log
		Log.apiLog.info(json);

		response.setContentType("application/json;charset=UTF-8");

		try (PrintWriter out = response.getWriter()) {

			out.write(json);

		}

		response.flushBuffer();
	}

	private ApiBeanStore FillApiStore(Request req) {

		ApiBeanStore apiStore = null;

		for (ApiBeanStore store : ApiBeanStore.StoreList) {

			if (store.getMethodKey().equals(req.getMethod().toLowerCase())) {
				apiStore = store;
				continue;
			}

		}
		return apiStore;
	}

	private Request FillApiRequest(String reqContent) {
		try {

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));

			// @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
			Request req = objectMapper.readValue(reqContent, Request.class);
			return req;

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		return null;

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
