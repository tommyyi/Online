package com.tianyi.tool.data;

/**
 * @date 	September 12, 2012 
 * @author Administrator
 */
public enum NetworkType {
	NORMAL("",null,0), // 3G or wifi
	CMWAP("cmwap", "10.0.0.172", 80), 
	CTWAP("ctwap", "10.0.0.200", 80),
	UNIWAP("uniwap", "10.0.0.172", 80),
	WIFI("wifi", "", 0);
	
	private final String value;
	private final String host;
	private final int port;
	
	public String getValue() {
		return value;
	}
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	public boolean equals(String type) {
		return (getValue().equals(type));
	}
	
	private NetworkType(String value, String host, int port) {
		this.value = value;
		this.host = host;
		this.port = port;
	}
}
