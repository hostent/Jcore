package com.jcore.Mongodb;

import java.time.LocalDateTime;
import java.util.Date;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.Marker;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongodbLog implements Logger{
	
	
	

	
	String _logName="";
	
	public MongodbLog(String logName)
	{
		_logName = logName;
	}
	
	// 这里应该修改成异步记录
	private void saveLog(String msg,String level)
	{
		
		new Runnable() {

			@Override
			public void run() {
				
				LogEntity entity = new LogEntity();
				entity.setLogType(_logName);
				entity.setLevel(level);
				entity.setLogTime(new Date());
				entity.setMsg(msg);
				
				MongoDatabase db = ConnectionManager.getDb("MongodbLog");
				
				MongoCollection<Document> col = db.getCollection("LogEntity", Document.class);
				col.insertOne(entity.getDoc());
			}
		
		}.run();
		
		

	}
	
	@Override
	public String getName() {
		// TODO 自动生成的方法存根
		return _logName;
	}

	@Override
	public boolean isTraceEnabled() {
		// TODO 自动生成的方法存根
		
		
		
		return false;
	}

	@Override
	public void trace(String msg) {
		if(isTraceEnabled())
		{
			saveLog(msg,"trace");
		}
		
	}

	@Override
	public void trace(String format, Object arg) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void trace(String format, Object... arguments) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void trace(String msg, Throwable t) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public boolean isTraceEnabled(Marker marker) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void trace(Marker marker, String msg) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void trace(Marker marker, String format, Object arg) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void trace(Marker marker, String format, Object... argArray) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void trace(Marker marker, String msg, Throwable t) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public boolean isDebugEnabled() {
		// TODO 自动生成的方法存根
		return true;
	}

	@Override
	public void debug(String msg) {
		if(isDebugEnabled())
		{
			saveLog(msg,"debug");
		}
		
	}

	@Override
	public void debug(String format, Object arg) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void debug(String format, Object... arguments) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void debug(String msg, Throwable t) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void debug(Marker marker, String msg) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void debug(Marker marker, String format, Object arg) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void debug(Marker marker, String format, Object... arguments) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void debug(Marker marker, String msg, Throwable t) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public boolean isInfoEnabled() {
		// TODO 自动生成的方法存根
		return true;
	}

	@Override
	public void info(String msg) {
		if(isInfoEnabled())
		{
			saveLog(msg,"info");
		}
		
	}

	@Override
	public void info(String format, Object arg) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void info(String format, Object... arguments) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void info(String msg, Throwable t) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void info(Marker marker, String msg) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void info(Marker marker, String format, Object arg) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void info(Marker marker, String format, Object arg1, Object arg2) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void info(Marker marker, String format, Object... arguments) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void info(Marker marker, String msg, Throwable t) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public boolean isWarnEnabled() {
		// TODO 自动生成的方法存根
		return true;
	}

	@Override
	public void warn(String msg) {
		if(isWarnEnabled())
		{
			saveLog(msg,"warn");
		}
		
	}

	@Override
	public void warn(String format, Object arg) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void warn(String format, Object... arguments) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void warn(String msg, Throwable t) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public boolean isWarnEnabled(Marker marker) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void warn(Marker marker, String msg) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void warn(Marker marker, String format, Object arg) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void warn(Marker marker, String format, Object... arguments) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void warn(Marker marker, String msg, Throwable t) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public boolean isErrorEnabled() {
		// TODO 自动生成的方法存根
		return true;
	}

	@Override
	public void error(String msg) {
		if(isErrorEnabled())
		{
			saveLog(msg,"error");
		}
		
	}

	@Override
	public void error(String format, Object arg) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void error(String format, Object... arguments) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void error(String msg, Throwable t) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public boolean isErrorEnabled(Marker marker) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void error(Marker marker, String msg) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void error(Marker marker, String format, Object arg) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void error(Marker marker, String format, Object arg1, Object arg2) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void error(Marker marker, String format, Object... arguments) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void error(Marker marker, String msg, Throwable t) {
		// TODO 自动生成的方法存根
		
	}

}
