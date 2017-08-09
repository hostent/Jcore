package com.jcore.Frame;

public class Result {
	
	private int status;
	
	private String message;
	
	private Object data;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	
	public static Result get(int status,String message,Object data)
	{
		Result result = new Result();
		result.setData(data);
		result.setMessage(message);
		result.setStatus(status);
		
		return result;
	}

	public static Result get(int status,String message)
	{
		return get(status,message,null);
	}
	
	public static Result get(int status)
	{
		return get(status,null,null);
	}

	
	public static Result succeed()
	{
		return get(1);
	}
	public static Result succeed(Object data)
	{
		return get(1,"",data);
	}
	public static Result failure()
	{
		return get(-1);
	}
	public static Result failure(String message)
	{
		return get(-1,message);
	}
}
