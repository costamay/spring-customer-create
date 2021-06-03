package com.kcbgroup.customer.commons.exceptions;

import com.kcbgroup.customer.commons.ErrorCodes;

/**
 * Data Validation Exception Class
 * 
 * Gideon Mulandi | KCB App Dev
 * @version 0.0.1
 */
public class SystemAuthenticationException extends Exception {
	/** Serial version */
	private static final long serialVersionUID = 5984136394820807294L;
	
    private String errorCode;

	public SystemAuthenticationException() {
		super();
	}

	public SystemAuthenticationException(String message) {
		super(message);
	}
	
	public SystemAuthenticationException(ErrorCodes errorCode) {
		super();
		this.errorCode = errorCode.name();
	}

	public SystemAuthenticationException(String message, ErrorCodes errorCode) {
		super(message);
		this.errorCode = errorCode.name();
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
