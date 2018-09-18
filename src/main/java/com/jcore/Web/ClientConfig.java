package com.jcore.Web;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;

import com.jcore.Tool.PropertiesHelp;
import com.jcore.Zookeeper.ServerListWatcher;
import com.jcore.Zookeeper.ServerNode;
 

public class ClientConfig {

	public ClientConfig(String targetSystem) {
		try {
			
			if(hasUrl(targetSystem))
			{
				this.set_key(PropertiesHelp.getAppConf("client." + targetSystem + ".key"));
				this.set_secret(PropertiesHelp.getAppConf("client." + targetSystem + ".secret"));
				this.set_url(PropertiesHelp.getAppConf("client." + targetSystem + ".url"));
				
				if(_key.equals("") || _secret.equals(""))
				{
					set_isSign(false);
				}

			}
			else
			{
				// zookeeper;
				
				ServerNode sn =	ServerListWatcher.watch(targetSystem);
				
				this.set_url(sn.getAddress()+":"+sn.getPort());
			}
		
			
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	private boolean hasUrl(String targetSystem) throws IOException
	{
		return ! PropertiesHelp.getAppConf("client." + targetSystem + ".url").equals("");
	}

	private String _url = "";
	private String _secret = "";
	private String _key = "";
	
	private Boolean _isSign=true;

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

	public Boolean get_isSign() {
		return _isSign;
	}

	public void set_isSign(Boolean _isSign) {
		this._isSign = _isSign;
	}

}
