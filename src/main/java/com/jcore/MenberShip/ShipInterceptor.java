package com.jcore.MenberShip;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class ShipInterceptor implements HandlerInterceptor {
		
	
	String ss="";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO 自动生成的方法存根
		
		Method method = ((HandlerMethod)handler).getMethod();
		
		Right right=method.getAnnotation(Right.class);
		if(right!=null)
		{
			if(!UserContext.createInstance(request, response).checkRight(right.value()))
			{
				return false;
			}
		}		
		
		
		ss="fdsafsad";
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO 自动生成的方法存根
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO 自动生成的方法存根
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	
	

}
