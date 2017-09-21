package com.jcore.Web;

import java.io.IOException;
import java.security.MessageDigest;

import com.jcore.Tool.Md5Help;
import com.jcore.Tool.PropertiesHelp;

public class Auth {

	private String key;

	private String secret;

	public Auth(String key) {
		this.setKey(key);
		try {
			this.setSecret(PropertiesHelp.getAppConf("api.secret." + key));
		} catch (IOException e) {
			this.setSecret(null);
			e.printStackTrace();
		}
	}

 
  
	public boolean checkAuth(String sign, String json) {
		if (secret == null || secret.isEmpty()) {
			// 没有配置，就是不校验
			return true;
		}

		String calcSign = Md5Help.toMD5(key + secret + json.trim());

		if (!calcSign.toLowerCase().equals(sign.toLowerCase())) {
			return false;
		}

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
