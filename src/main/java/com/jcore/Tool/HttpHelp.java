package com.jcore.Tool;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.*;
import java.security.cert.*;
import java.util.*;

import javax.net.ssl.SSLContext;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
 
import org.apache.http.ssl.*;
import org.apache.http.util.EntityUtils;

import com.jcore.Frame.Log;
 

 

public class HttpHelp {	
	  	
	
	static CloseableHttpClient createClientDefault(boolean isSSL){

		try {

			if(isSSL)
			{
				 SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

	                 //信任所有

	                 public boolean isTrusted(X509Certificate[] chain,

	                                 String authType) throws CertificateException {

	                     return true;

	                 }

	             }).build();

	             SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
	             
	             return HttpClients.custom().setSSLSocketFactory(sslsf).build();
			}
			else
			{
				return HttpClients.custom().build();
			}
		            

		            

		         } catch (KeyManagementException e) {

		        	 Log.logError(e);

		         } catch (NoSuchAlgorithmException e) {

		        	 Log.logError(e);

		         } catch (KeyStoreException e) {

		        	 Log.logError(e);

		         }

		         return  HttpClients.createDefault();

		}

	public static String post(String url,HashMap<String,Object> hmList) throws URISyntaxException, ClientProtocolException, IOException
	{
		boolean isSsl = url.toLowerCase().startsWith("https://");
		CloseableHttpClient httpClient = HttpHelp.createClientDefault(isSsl);	 

		HttpPost post = new HttpPost();

		post.setURI(new URI(url));
		
		if(hmList!=null && hmList.size() > 0){ 
			
			
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"utf-8");  
			
			for (String key : hmList.keySet()) {  
				
				list.add(new NameValuePair() {
					
					@Override
					public String getValue() {
						 
						return hmList.get(key).toString();
					}					
					@Override
					public String getName() {
						 
						return key;
					}
				});
			} 				
            
            post.setEntity(entity);  
        }  
		HttpResponse response = httpClient.execute(post);
		
		if(response != null){  
            HttpEntity resEntity = response.getEntity();  
            if(resEntity != null){  
            	String    result = EntityUtils.toString(resEntity,"utf-8"); 
            	
            	return result;
            }  
        }  
		
		return "";
	}

	
	public static String post(String url,String json) throws URISyntaxException, ClientProtocolException, IOException
	{
		boolean isSsl = url.toLowerCase().startsWith("https://");
		CloseableHttpClient httpClient = HttpHelp.createClientDefault(isSsl);	 

		HttpPost post = new HttpPost();

		post.setURI(new URI(url));
		
		StringEntity se = new StringEntity(json);
		
		post.setEntity(se);
		 
		HttpResponse response = httpClient.execute(post);
		
		if(response != null){  
            HttpEntity resEntity = response.getEntity();  
            if(resEntity != null){  
            	String    result = EntityUtils.toString(resEntity,"utf-8"); 
            	
            	return result;
            }  
        }  
		
		return "";
	}

}
