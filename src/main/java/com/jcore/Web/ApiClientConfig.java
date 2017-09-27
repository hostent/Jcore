package com.jcore.Web;

import java.io.IOException;

import com.jcore.Tool.PropertiesHelp;

public class ApiClientConfig {

	public ApiClientConfig(String targetSystem) {
		try {

			this.set_key(PropertiesHelp.getAppConf("client." + targetSystem + ".key"));
			this.set_secret(PropertiesHelp.getAppConf("client." + targetSystem + ".secret"));
			this.set_url(PropertiesHelp.getAppConf("client." + targetSystem + ".url"));

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private String _url = "";
	private String _secret = "";
	private String _key = "";

	public String get_url() {
		return _url;
	}

	public void set_url(String _url) {
		this._url = _url;
	}

	public String get_secret() {
		return _secret;
	}

	public void set_secret(String _secret) {
		this._secret = _secret;
	}

	public String get_key() {
		return _key;
	}

	public void set_key(String _key) {
		this._key = _key;
	}

}
