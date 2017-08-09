package com.jcore.Tool;


import java.io.IOException;
import java.util.Properties;

public class PropertiesHelp {

	static Properties dbPro = null;

	private static Object flagLock="1";
	
	static Properties appPro = null;

	private static Object flagLock2="2";

	public static String getDbConf(String key) throws IOException {
		if (dbPro == null) {
			synchronized (flagLock) {
				if(dbPro == null)
				{		
					
					dbPro = new Properties();					
					
					dbPro.load(PropertiesHelp.class.getClassLoader().getResourceAsStream("db.properties"));
 
					
					flagLock=11;
				}				
			}
		}

		return dbPro.getProperty(key, "");

	}

	
	
	
	public static String getAppConf(String key) throws IOException {
		if (appPro == null) {
			synchronized (flagLock2) {
				if(appPro == null)
				{	
					appPro = new Properties();					
					
					appPro.load(PropertiesHelp.class.getClassLoader().getResourceAsStream("appSetting.properties"));
					
					flagLock2=2;
				}				
			}
		}

		return appPro.getProperty(key, "");

	}

}
