package com.jcore.Frame;

import java.io.*;

import org.slf4j.*;

import com.jcore.Mongodb.MongodbLog;
import com.jcore.Tool.PropertiesHelp;

public class Log {

	public final static Logger msgLog = getLogger("com.jcore.log.info");

	public final static Logger apiLog = getLogger("com.jcore.log.api");

	public final static Logger proxyLog = getLogger("com.jcore.log.proxy");
	
	public final static Logger errorLog = getLogger("com.jcore.log.error");
	
	public final static Logger getLogger(String logName)
	{
		String logType = "";
		try {
			logType = PropertiesHelp.getApplicationConf("com.jcore.log.type");
		} catch (IOException e) {
			logType="";
		}
		
		if(logType.equals("mongodbLog"))
		{
			return new MongodbLog(logName);
		}
		return LoggerFactory.getLogger(logName);
		
	}
	
	public final static String getErrorMsg(Exception e)
	{
		StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw =  new PrintWriter(sw);
            //将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
        return sw.toString();
	}

	public final static void logError(Exception e)
	{
		errorLog.error(getErrorMsg(e));
	}
	public final static void logError(Exception e,String msg)
	{
		errorLog.error(getErrorMsg(e)+msg);
	}

}
