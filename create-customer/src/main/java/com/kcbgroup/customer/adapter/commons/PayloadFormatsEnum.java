package com.kcbgroup.customer.adapter.commons;

/**
 * REST Payload Enum
 * 
 * @author Francisco Osorio | Bring Global
 * @version 0.0.1
 * @implNote Only for the most popular formats (JSON, XML AND TEXT)
 */
public enum PayloadFormatsEnum {
	TEXT("text/plain"), 
	CSV("text/csv"), 
	JSON("application/json"),
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
