package com.jcore.Zookeeper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerNode {
	
	private String id;

	private String name;

	private String address;

	private int port;

	@JsonProperty(value = "id")
	public String getId() {
		return id;
	}

	@JsonProperty(value = "id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty(value = "name")
	public String getName() {
		return name;
	}

	@JsonProperty(value = "name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty(value = "address")
	public String getAddress() {
		return address;
	}
	
	@JsonProperty(value = "address")
	public void setAddress(String address) {
		this.address = address;
	}

	@JsonProperty(value = "port")
	public int getPort() {
		return port;
	}

	@JsonProperty(value = "port")
	public void setPort(int port) {
		this.port = port;
	}

}
