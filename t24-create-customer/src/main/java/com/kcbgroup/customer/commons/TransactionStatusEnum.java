package com.kcbgroup.customer.commons;

/**
 * Response Status Enum
 * 
 * @author Francisco Osorio | Bring Global
 * @version 0.0.1
 * @implNote Only for response
 */
public enum TransactionStatusEnum {
	FAILURE("1", "Failure"), 
	SUCCESS("0", "Success");

	private String code;
	private String description;

	private TransactionStatusEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return this.code;
	}
	
	public String getDescription() {
		return this.description;
	}
}