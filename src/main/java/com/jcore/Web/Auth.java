package com.jcore.Web;

public class Auth {
	
	private String key;
	
	private String secret;
	
	public Auth() {
		
	}

	public boolean checkAuth(String key ,String sign,String json)
	{
		//sign =md5( key + secret + json)
		//url: restapi?key=hfht&token=xxxxxxxxxxxxx
		
		
		return true;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
}
