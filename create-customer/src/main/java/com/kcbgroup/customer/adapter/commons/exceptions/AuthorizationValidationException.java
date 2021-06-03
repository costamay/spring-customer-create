package com.kcbgroup.customer.adapter.commons.exceptions;

import com.kcbgroup.customer.adapter.commons.ErrorCodes;

/**
 * Authorization Validation Exception Class
 * 
 * @author Francisco Osorio | Bring Global
 * @version 0.0.1
 */
public class AuthorizationValidationException extends Exception {
	/** Serial version */
	private static final long serialVersionUID = 5984136394820807294L;

	private String errorCode;

	public AuthorizationValidationException() {
		super();
	}

	public AuthorizationValidationException(String message) {
		super(message);
	}

	public AuthorizationValidationException(ErrorCodes errorCode) {
		super();
		this.errorCode = errorCode.name();
	}

	public AuthorizationValidationException(String message, ErrorCodes errorCode) {
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
