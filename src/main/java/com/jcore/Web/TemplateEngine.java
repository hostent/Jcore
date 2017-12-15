package com.jcore.Web;

import java.io.IOException;
import java.io.StringWriter;

import java.util.HashMap;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.jcore.Frame.Log;
import com.jcore.Tool.PropertiesHelp;

public class TemplateEngine {

	public static String raw(String path, HashMap<String, Object> dict) {

		String encoding="";
		String pathSub="";
		String suffix="";
		try {
			encoding = PropertiesHelp.getApplicationConf("spring.velocity.charset");
		 
			pathSub = PropertiesHelp.getApplicationConf("spring.velocity.resource-loader-path")
					.replace("classpath:/", "");
		 
			suffix = PropertiesHelp.getApplicationConf("spring.velocity.suffix");
		} catch (IOException e) {

			Log.logError(e);
			
			return "模板引擎配置错误";
		}

		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

		ve.init();

		Template t = ve.getTemplate(pathSub + path + suffix, encoding);
		VelocityContext ctx = new VelocityContext();

		if(dict!=null)
		{
			for (String key : dict.keySet()) {

				ctx.put(key, dict.get(key));
			}

		}
		
		// ctx.put("name", "velocity");

		StringWriter sw = new StringWriter();

		t.merge(ctx, sw);

		return sw.toString();
	}

}
