package com.jcore.Web;

import java.io.IOException;
import java.security.MessageDigest;

import com.jcore.Tool.PropertiesHelp;

public class Auth {

	private String key;

	private String secret;

	public Auth(String key) {
		this.setKey(key);
		try {
			this.setSecret(PropertiesHelp.getAppConf("api.secret." + key));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static String getMD5(String message) {
		String md5str = "";
		try {
			// 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
			MessageDigest md = MessageDigest.getInstance("MD5");

			// 2 将消息变成byte数组
			byte[] input = message.getBytes("utf-8");

			// 3 计算后获得字节数组,这就是那128位了
			byte[] buff = md.digest(input);

			// 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
			md5str = bytesToHex(buff);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5str;
	}

	static String bytesToHex(byte[] bytes) {
		StringBuffer md5str = new StringBuffer();
		// 把数组每一字节换成16进制连成md5字符串
		int digital;
		for (int i = 0; i < bytes.length; i++) {
			digital = bytes[i];

			if (digital < 0) {
				digital += 256;
			}
			if (digital < 16) {
				md5str.append("0");
			}
			md5str.append(Integer.toHexString(digital));
		}
		return md5str.toString().toUpperCase();
	}

	public boolean checkAuth(String sign,String json)
	{
		
		String calcSign =getMD5( key + secret + json);
		//url: restapi?key=hfht&token=xxxxxxxxxxxxx
		if(calcSign.toLowerCase()!=sign.toLowerCase())
		{
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
