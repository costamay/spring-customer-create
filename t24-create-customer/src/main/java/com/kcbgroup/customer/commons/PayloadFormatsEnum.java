package com.kcbgroup.customer.commons;

/**
 * REST Payload Enum
 * 
 * Gideon Mulandi | KCB App Dev
 * @version 0.0.1
 * @implNote Only for the most popular formats (JSON, XML AND TEXT)
 */
public enum PayloadFormatsEnum {
	TEXT("text/plain"), 
	CSV("text/csv"), 
	JSON("application/json"),
	TEXT_XML("text/xml"),
	XML("application/xml");

	/** The content type of te selected format */
	private String contentType;

	private PayloadFormatsEnum(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return this.contentType;
	}
}
