package com.jcore.Tool;


import java.io.IOException;
import java.util.Properties;

public class PropertiesHelp {

	static Properties dbPro = null;

	private static Object flagLock="1";
	
	static Properties appPro = null;

	private static Object flagLock2="2";
	
	static Properties applicationPro = null;
	
	private static Object flagLock3="3";

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

	
	public static String getApplicationConf(String key) throws IOException {
		if (applicationPro == null) {
			synchronized (flagLock3) {
				if(applicationPro == null)
				{	
					applicationPro = new Properties();					
					
					applicationPro.load(PropertiesHelp.class.getClassLoader().getResourceAsStream("application.properties"));
					
					flagLock3=3;
				}				
			}
		}

		return applicationPro.getProperty(key, "");

	}

}
