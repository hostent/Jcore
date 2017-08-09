package com.jcore.Frame;

import java.io.*;

import org.slf4j.*;

public class Log {

	public final static Logger msgLog = LoggerFactory.getLogger("com.jcore.log.info");

	public final static Logger apiLog = LoggerFactory.getLogger("com.jcore.log.api");

	public final static Logger proxyLog = LoggerFactory.getLogger("com.jcore.log.proxy");
	
	public final static Logger errorLog = LoggerFactory.getLogger("com.jcore.log.error");
	
	public final static Logger getLogger(String logName)
	{
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

}
