package com.jcore.Web;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

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

		Request apiReq = FillApiRequest(request);
		Response apiResponse = new Response();

		if (apiReq == null) {
			apiResponse.setError("-90:传输数据错误");
			outPutResponse(response, apiResponse);
			return;
		}

		apiResponse.setId(apiReq.getId());

		ApiBeanStore apiStore = FillApiStore(apiReq);

		if (apiStore == null) {
			apiResponse.setError("-100:找不到方法");
			outPutResponse(response, apiResponse);
			return;
		}

		apiReq.setParams(apiStore.TypeOfArgs(apiReq.getParams())); // 类型化参数

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

	private Request FillApiRequest(ServletRequest request) {
		try {
			int totalbytes = request.getContentLength();
			if (totalbytes <= 0) {
				return null;
			}
			byte[] dataOrigin = new byte[totalbytes];

			DataInputStream in = new DataInputStream(request.getInputStream());
			in.readFully(dataOrigin);
			in.close();

			String reqContent = new String(dataOrigin);
			if (reqContent == null || reqContent.isEmpty()) {
				return null;
			}

			ObjectMapper objectMapper = new ObjectMapper();
			Request req = objectMapper.readValue(reqContent, Request.class);
			return req;

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}

	@Override
	public void destroy() {
		System.out.println("xxxxxxxxxxxxxxx:destroy");
	}

}
