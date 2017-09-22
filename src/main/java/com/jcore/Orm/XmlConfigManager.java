package com.jcore.Orm;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.URLDecoder;
 

public class XmlConfigManager {

	private static Element RepsConfig = null;

	public static Element getRepsConfig() {
		if (RepsConfig != null) {
			return RepsConfig;
		}

		String rootXml = "<Reps></Reps>";

		try {
			String repsPath = XmlConfigManager.class.getClassLoader().getResource("").getFile() + "XmlReport";
			
			try {
				repsPath = URLDecoder.decode(repsPath,"utf-8");
				
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}
			
			File file = new File(repsPath);
			if (file.isDirectory()) {
				File[] fileArray = file.listFiles();
				for (int i = 0; i < fileArray.length; i++) {
					if (fileArray[i].getName().endsWith(".Reps.xml")) {
						
						SAXReader reader = new SAXReader();
						Document doc;

						doc = reader.read(fileArray[i]);

						Element repItem = doc.getRootElement().element("Rep");

						rootXml = rootXml.replace("</Reps>", repItem.asXML() + "</Reps>");

					}
				}
			}

			Document documentRoot;

			documentRoot = DocumentHelper.parseText(rootXml);

			RepsConfig = documentRoot.getRootElement();

		} catch (DocumentException e) {

			e.printStackTrace();
		}

		return RepsConfig;

	}
}
