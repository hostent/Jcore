package com.jcore.Frame;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {
	 private String Id ;

	 private String Method ;

	 private Object[] Params;

	 @JsonProperty(value = "Id") 
	public String getId() {
		return Id;
	}

	 @JsonProperty(value = "Id") 
	public void setId(String id) {
		Id = id;
	}

	 @JsonProperty(value = "Method") 
	public String getMethod() {
		return Method;
	}

	 @JsonProperty(value = "Method") 
	public void setMethod(String method) {
		Method = method;
	}

	 @JsonProperty(value = "Params") 
	public Object[] getParams() {
		return Params;
	}

	 @JsonProperty(value = "Params") 
	public void setParams(Object[] params) {
		Params = params;
	}
}
