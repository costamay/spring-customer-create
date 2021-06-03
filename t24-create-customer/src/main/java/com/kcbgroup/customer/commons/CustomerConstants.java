 package com.kcbgroup.customer.commons;

import org.apache.camel.builder.xml.Namespaces;

/**
 * Account Info Constants Class
 * 
 * @author Gideon Mulandi | KCB App Dev
 * @version 1.0.0
 */
public class CustomerConstants {


	public static final String ORIGINAL_MESSAGE_PROPERTY = "originalMessage";
	
	public static final Integer SIZE_MESSAGES = 150;
	
	public static final String TARGET_SYSTEM_ID_VALUE = "T24";
	public static final String GENERAL_SYSTEM_ERROR_VALUE = "Target System Down. Try again after sometime";
	public static final String SECURITY_VIOLATION_MESSAGE_VALUE = "SECURITY VIOLATION";
	public static final String BAD_NAME_SUPPLIED_MESSAGE_VALUE = "NO SIGN ON NAME";
	
	public static final String VAULT_PASSWORD_HEADER = "password";
	
	public static final String REQUEST_T24_OPEDAND_HEADER = "operand";
	
	public static final String REQUEST_T24_COLUMNNAME_HEADER = "columnName";

	public static final String RESPONSE_STATUS_CODE_FAILURE = "1";
	
	
	public static final String CONVERSATION_ID_HEADER = "conversationID";
	public static final String MESSAGE_ID_HEADER = "messageID";
	public static final String RESULT_CODE_HEADER = "resultCode";
	public static final String ROUTE_CODE_HEADER = "routeCode";

	
	public static final Namespaces t24Namespaces = new Namespaces("S", "http://schemas.xmlsoap.org/soap/envelope/")
			.add("ns2", "http://temenos.com/ACCOUNT")
			.add("ns3", "http://temenos.com/CUSTOMERKCBCHANNELS")
			.add("ns4", "http://temenos.com/CUSTOMER")
			.add("ns5", "http://temenos.com/ACCOUNTKCBCHANNELS")
			.add("ns6", "http://temenos.com/T24KCBAccountOpening");
	
}
