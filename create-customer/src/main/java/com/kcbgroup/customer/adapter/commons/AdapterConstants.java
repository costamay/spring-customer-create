package com.kcbgroup.customer.adapter.commons;

/**
 * Adapter Constants Class
 * 
 * @author Francisco Osorio | Bring Global
 * @version 0.0.1
 */
public class AdapterConstants {
	
	public static final String CONVERSATION_ID_HEADER = "conversationID";
	public static final String MESSAGE_ID_HEADER = "messageID";
	public static final String CHANNEL_CODE_HEADER = "channelCode";
	public static final String JMS_DELIVERY_MODE_HEADER = "JMSDeliveryMode";
	public static final String JMS_PRIORITY_HEADER = "JMSPriority";
	public static final String JMS_CORRELATION_ID_HEADER = "JMSCorrelationID";
	public static final String SERVICE_SECURITY_DEFINITION_BASIC_VALUE = "Basic";
	public static final String WWW_AUTHENTICATE_HEADER_VALUE = "Basic realm=\"Access to MS Adapter Layer\"";
	public static final String SERVICE_AUTHORIZATION_HEADER_NAME = "Authorization";
	public static final String SERVICE_HEADER_REPLY_TO_QUEUE = "replyToQueueName";
	public static final String SERVICE_HEADER_SEND_TO_QUEUE = "sendToQueueName";
	public static final String RESPONSE_STATUS_DESCRIPTION_FAILURE = "Failure";
	public static final String RESPONSE_STATUS_CODE_FAILURE = "1";
	
	public static final String ROUTE_CODE_HEADER = "routeCode";
	public static final String SERVICE_CODE_HEADER = "serviceCode";
	
	
	public static final String ORIGINAL_MESSAGE_PROPERTY = "originalMessage";
	public static final String UNMARSHALED_MESSAGE_PROPERTY = "unmarshaledMessage";
	
	/* Only unit test*/
	public static final String VAULT_USERNAME_HEADER = "vaultUser";
	public static final String VAULT_PASSWORD_HEADER = "vaultPassword";
}
