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
			this.setSecret(null);
			e.printStackTrace();
		}
	}

 
 
	String toMD5(String plainText) {
		try {
			// 生成实现指定摘要算法的 MessageDigest 对象。
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 使用指定的字节数组更新摘要。
			md.update(plainText.getBytes("utf-8"));
			// 通过执行诸如填充之类的最终操作完成哈希计算。
			byte b[] = md.digest();
			// 生成具体的md5密码到buf数组
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public boolean checkAuth(String sign, String json) {
		if (secret == null || secret.isEmpty()) {
			// 没有配置，就是不校验
			return true;
		}

		String calcSign = toMD5(key + secret + json.trim());

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
