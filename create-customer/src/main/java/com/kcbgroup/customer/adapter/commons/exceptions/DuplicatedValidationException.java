package com.kcbgroup.customer.adapter.commons.exceptions;

import com.kcbgroup.customer.adapter.commons.ErrorCodes;

/**
 * create-customer
 * Oct 7, 2020
 * DuplicatedValidationException.java
 *
 * @author Andrés Vázquez | Bring global - KCB
 * @version 1.0.0
 */
public class DuplicatedValidationException extends Exception {
	
	/** Serial version */
	private static final long serialVersionUID = 5984136394820807294L;
	
	private String errorCode;

	public DuplicatedValidationException() {
		super();
	}

	public DuplicatedValidationException(String message) {
		super(message);
	}

	public DuplicatedValidationException(ErrorCodes errorCode) {
		super();
		this.errorCode = errorCode.name();
	}

	public DuplicatedValidationException(String message, ErrorCodes errorCode) {
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