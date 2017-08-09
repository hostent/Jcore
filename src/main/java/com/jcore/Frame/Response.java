package com.jcore.Frame;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {

	private String Id ;

	private Object Result;

	private String Error;
    
	 @JsonProperty(value = "Id") 
	public String getId() {
		return Id;
	}

	 @JsonProperty(value = "Id") 
	public void setId(String id) {
		Id = id;
	}
 
	 
	 @JsonProperty(value = "Result") 
	public Object getResult() {
		return Result;
	}

	 @JsonProperty(value = "Result") 
	public void setResult(Object result) {
		Result = result;
	}

	 @JsonProperty(value = "Error") 
	public String getError() {
		return Error;
	}

	 @JsonProperty(value = "Error") 
	public void setError(String error) {
		Error = error;
	}
	

}
