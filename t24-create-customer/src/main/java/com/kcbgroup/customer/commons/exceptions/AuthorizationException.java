package com.kcbgroup.customer.commons.exceptions;

import com.kcbgroup.customer.commons.ErrorCodes;

public class AuthorizationException extends Exception {
	/** Serial version */
	private static final long serialVersionUID = 5984136394820807294L;

	private String errorCode;

	public AuthorizationException() {
		super();
	}

	public AuthorizationException(String message) {
		super(message);
	}

	public AuthorizationException(ErrorCodes errorCode) {
		super();
		this.errorCode = errorCode.name();
	}

	public AuthorizationException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
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
