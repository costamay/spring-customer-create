package com.kcbgroup.customer.adapter.commons;

/**
 * HTTP Errors Enum
 * 
 * @author Francisco Osorio | Bring Global
 * @version 0.0.1
 * @implNote Only for the exceptions of this project.
 *           See @ke.com.adapter.common.exceptions
 */
public enum HTTPResponseCodesEnum {
	BAD_REQUEST(400), // For Validation Exception
	INTERNAL_SERVER_ERROR(500), // For Any Camel Exception
	UNAUTHORIZED(401), // For Authentication Exception
	CONFLICT(409), // For Duplicated Exception
    POST_SUCCESS(201); // Created
	
	/** The http response code */
	private Integer responseCode;

	private HTTPResponseCodesEnum(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public Integer getResponseCode() {
		return responseCode;
	}
}
