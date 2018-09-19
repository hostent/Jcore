package com.jcore.MenberShip;

import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.jcore.redis.Cache;

public class UserContext {

	@Autowired	
	HttpServletRequest _request;
	@Autowired
	HttpServletResponse _response;
		
	final String cookieKey="jfkldsf6873938dzkjfdsl33";
	
	public static UserContext createInstance(HttpServletRequest request,HttpServletResponse response)
	{
		UserContext userContext = new UserContext();
		userContext._request=request;
		userContext._response=response;
		return userContext;
	}
	
	public String getLoginKey()
	{	
		if(_request==null)
		{
			return null;
		}
		Cookie[] cookies = _request.getCookies();
		if(cookies==null || cookies.length==0)
		{
			return null;
		}
		for (Cookie cookie : _request.getCookies()) {
			if(cookie.getName().equals(cookieKey))
			{
				return cookie.getValue();
			}
		}
		return null;
	}	
	public void setLoginKey(String key)
	{
		if(_response==null)
		{
			return;
		}
		Cookie cookie = new Cookie(cookieKey,key);
		_response.addCookie(cookie);
	}

	public IUser getUserInfo()
	{
		String loginKey =getLoginKey();
		if(loginKey.isEmpty()||loginKey==null)
		{
			return null;
		}
		String json =Cache.get("UserContext.UserInfo."+loginKey);		
		return JSON.parseObject(json, IUser.class);
		
	}
	public void setUserInfo(IUser user)
	{
		String loginKey =getLoginKey();
		if(loginKey.isEmpty()||loginKey==null)
		{
			return;
		}
		String json =JSON.toJSONString(user);
		
		Cache.set("UserContext.UserInfo."+loginKey, json);
	}
	
	public List<IMenu> getMenuList()
	{
		String loginKey =getLoginKey();
		if(loginKey.isEmpty()||loginKey==null)
		{
			return null;
		}
		String json =Cache.get("UserContext.MenuList."+loginKey);		
		return JSON.parseArray(json, IMenu.class);
	}
	public void setMenuList(List<IMenu> list)
	{
		String loginKey =getLoginKey();
		if(loginKey.isEmpty()||loginKey==null)
		{
			return;
		}
		String json =JSON.toJSONString(list);		
		Cache.set("UserContext.MenuList."+loginKey, json);
	}
	
	public List<IGroup> getGropList()
	{
		String loginKey =getLoginKey();
		if(loginKey.isEmpty()||loginKey==null)
		{
			return null;
		}
		String json =Cache.get("UserContext.GropList."+loginKey);		
		return JSON.parseArray(json, IGroup.class);
	}
	public void setGropList(List<IGroup> list)
	{
		String loginKey =getLoginKey();
		if(loginKey.isEmpty()||loginKey==null)
		{
			return;
		}
		String json =JSON.toJSONString(list);		
		Cache.set("UserContext.GropList."+loginKey, json);
	}
	
	
	public Boolean isLogin()
	{
		if(getUserInfo()==null)
		{
			return false;
		}
		return true;
	}

	public Boolean checkRight(String rightKey)
	{
		if(!isLogin())
		{
			return false;
		}
		for (IMenu menu : getMenuList()) {
			
			if(menu.getKey().toLowerCase().equals(rightKey.toLowerCase()))
			{
				return true;
			}
		}
		return false;
	}
	
}
