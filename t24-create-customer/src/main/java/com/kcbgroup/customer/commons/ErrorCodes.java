package com.kcbgroup.customer.commons;

public enum ErrorCodes {

	OSP1000("Caller authentication error","OSP-1000"),
	OSP1002("Processed Successfully","OSP-1002"),
	OSP1001("System authentication error","OSP-1001"),
	OSP1003("System internal server error","OSP-1003"),
	OSP1004("System unavailable","OSP-1004"),
	OSP1005("System timedout","OSP-1005"),
	OSP1006("System service unavailable","OSP-1006"),
	OSP1007("System operation unavailable","OSP-1007"),
	OSP1008("System down","OSP-1008"),
	OSP1009("System payload error","OSP-1009"),
	OSP1010("System missing security details","OSP-1010"),
	OSP1011("System busy","OSP-1011"),	
	OSP1012("Queue does not exist","OSP-1012"),
	OSP1013("Duplicate message identifier","OSP-1013"),
	OSP1014("Message validation failed","OSP-1014"),
	OSP1999("Unknown error","OSP-1999"),
	OSP2013("Generic business error","OSP-2013");


	private String message;
	private String code;
	/**
	 * @param description
	 * @param code
	 */
	private ErrorCodes(String message, String code) {
		this.message = message;
		this.code = code;
	}
	/**
	 * @return the description
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
}