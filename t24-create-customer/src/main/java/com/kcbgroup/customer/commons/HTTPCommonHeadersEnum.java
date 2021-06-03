package com.kcbgroup.customer.commons;


/**
 * HTTP Common Headers Enum
 * 
 * Gideon Mulandi | KCB App Dev
 * @version 1.0.0
 * @implNote Only for the most common headers related to this project
 */
public enum HTTPCommonHeadersEnum {
	ACCEPT("Accept"),
	CONTENT_TYPE("Content-Type"),
	AUTHORIZATION("Authorization"),
	WWW_AUTHENTICATE("WWW-Authenticate");
	
	private String name;
	
	private HTTPCommonHeadersEnum(String name) {
		this.name = name;
	};
	
	public String getName() {
		return name;
	}
}